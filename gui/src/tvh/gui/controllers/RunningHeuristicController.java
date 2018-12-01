package tvh.gui.controllers;

import applicationEntrypoint.SolverMain;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import tvh.dataClasses.Location;
import tvh.interfaces.ScoreUpdater;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class RunningHeuristicController implements Initializable{

    @FXML private AnchorPane progressAnchor;
    @FXML private HBox progressBar;
    @FXML private HBox bestProgressBar;
    @FXML private Label clock;
    @FXML private Label scoreLabel;
    @FXML private Button runOrStop;

    private Timeline timeline;

    private Node node;
    private Thread heuristicThread;
    private String programArguments;
    private boolean running;
    private ScoreUpdater updater = new ScoreUpdater() {

        private double initial = -1;
        private double best = Double.MAX_VALUE;

        @Override
        public void locations(ArrayList<Location> locations) {

        }

        @Override
        public void done() {
            timeline.stop();
        }

        @Override
        public void updateScore(int score, long iteration) {
            if (initial == -1) initial = (double) score;

            if (score >= best) {
                Platform.runLater(() -> {
                    progressAnchor.setRightAnchor(
                            bestProgressBar,
                            progressAnchor.getBoundsInParent().getWidth() * (1 - best / initial)
                    );
                    bestProgressBar.setVisible(true);
                });
            } else {
                best = score;
                bestProgressBar.setVisible(false);
            }

            Platform.runLater(() -> data.getData().add(new XYChart.Data(iteration, score)));
            Platform.runLater(() -> {
                progressAnchor.setRightAnchor(
                        progressBar,
                        progressAnchor.getBoundsInParent().getWidth() * (1 - score / initial)
                );
                scoreLabel.setText(String.valueOf(score));
                if (progressAnchor.getBoundsInParent().getWidth() * (score / initial) > scoreLabel.getWidth()) {
                    progressAnchor.setRightAnchor(
                            scoreLabel,
                            progressAnchor.getBoundsInParent().getWidth() * (1 - score / initial)
                    );
                    scoreLabel.setStyle("-fx-text-fill: white");
                } else {
                    progressAnchor.setLeftAnchor(
                            scoreLabel,
                            progressAnchor.getBoundsInParent().getWidth() * (score / initial)
                    );
                    scoreLabel.setStyle("-fx-text-fill: black");
                }
            });
        }
    };
    private XYChart.Series<Number, Number> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        heuristicThread = new Thread(() -> {
            new SolverMain(updater).run(programArguments.split(" "));
        });
    }

    public static RunningHeuristicController initView(String threadArguments, String name) {
        RunningHeuristicController runningHeuristicController = null;
        try {
            FXMLLoader loader = new FXMLLoader(RunningHeuristicController.class.getResource(
                    "/tvh/views/running_heuristic_tab.fxml")
            );
            Node node = loader.load();
            runningHeuristicController = loader.getController();
            runningHeuristicController.programArguments = threadArguments;
            runningHeuristicController.running = false;
            runningHeuristicController.node = node;
            runningHeuristicController.data = new XYChart.Series<>();
            runningHeuristicController.data.setName(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return runningHeuristicController;
    }


    public void runOrStop(ActionEvent event) {
        if (running) {
            timeline.stop();
            heuristicThread.interrupt();
            ((VBox) node.getParent()).getChildren().remove(node);
            runOrStop.setText("run");
        } else {
            data.getData().clear();
            heuristicThread.start();
            long start = System.currentTimeMillis();
            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0), actionEvent -> {
                        clock.setText(new SimpleDateFormat("mm:ss")
                                .format(new Date(start - System.currentTimeMillis())));
                    }),
                    new KeyFrame(Duration.seconds(1))
            );
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
            running = true;
            runOrStop.setText("stop");
        }
    }

    public Node getNode() {
        return node;
    }

    public XYChart.Series<Number, Number> getData() {
        return data;
    }
}
