package tvh.gui.controllers;
import applicationEntrypoint.SolverMain;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import tvh.dataClasses.Location;
import tvh.interfaces.ScoreUpdater;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class TVHController implements Initializable {

    private ScoreUpdater updater = new ScoreUpdater() {

        private double initial = -1;
        private double best = Double.MAX_VALUE;

        @Override
        public void locations(ArrayList<Location> locations) {
            System.out.println("updater is connected");
        }

        @Override
        public void done() { timeline.stop(); }

        @Override
        public void updateScore(int score, long iteration) {
            if (initial == -1) initial = (double) score;
            if (score < best) {
                best = score;
                bestProgressBar.setVisible(false);
            } else {
                bestProgressBar.setVisible(true);
                Platform.runLater(() -> {
                    progressAnchor.setTopAnchor(
                            bestProgressBar,
                            progressAnchor.getBoundsInParent().getHeight() * (1 - best / initial)
                    );
                });
            }

            Platform.runLater(() -> scores.getData().add(new XYChart.Data(iteration, score)));
            Platform.runLater(() -> {
                progressAnchor.setTopAnchor(
                        progressBar,
                        progressAnchor.getBoundsInParent().getHeight() * (1 - score / initial)
                );
                scoreLabel.setText(String.valueOf(score));
                scoreAnchor.setTopAnchor(
                        scoreLabel,
                        scoreAnchor.getBoundsInParent().getHeight() * (1 - score / initial)
                );
            });
        }
    };

    public static final double lat1 = 2.5455857387;
    public static final double left = 120.0;
    public static final double lon1 = 49.496983;
    public static final double top = 43.0;
    public static final double lat2 = 6.408097;
    public static final double righr = 1185.0;
    public static final double lon2 = 51.5051500801;
    public static final double bottom = 912.0;

    @FXML private AnchorPane dataPane;
    @FXML private AnchorPane visualiserPane;
    @FXML private TabPane visualiserTab;
    @FXML private AnchorPane mapPane;
    @FXML private AnchorPane graphPane;
    @FXML private LineChart scoreGraph;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis score;
    @FXML private VBox progressBar;
    @FXML private AnchorPane progressAnchor;
    @FXML private AnchorPane scoreAnchor;
    @FXML private Label scoreLabel;
    @FXML private VBox bestProgressBar;
    @FXML private Label clock;

    private Timeline timeline;
    private long begin;

    private Thread solver;
    private XYChart.Series scores;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scoreGraph.setAnimated(false);
        scores = new XYChart.Series<Number, Number>();
        scores.setName("best score");
        scoreGraph.getData().addAll(scores);

        String[] args = new String[7];
        args[0] = String.format("--problem=%s", "C:\\dataFolder\\programmeren\\java\\Labo_Optimalisatie\\src\\data\\tvh_problem_4.txt");
        args[1] = String.format("--solution=%s", "C:\\dataFolder\\programmeren\\java\\Labo_Optimalisatie\\src\\data\\solution");
        args[2] = String.format("--seed=%s", "0");
        args[3] = String.format("--time=%s", "600");
        args[4] = String.format("--heuristic=%s", "simulated-annealing");
        args[5] = String.format("--dead-iteration-threshold=%s", "50");
        args[6] = String.format("--ruin-count=%S", "25");

        solver = new Thread(() -> new SolverMain(updater).run(args));
        solver.start();
        begin = System.currentTimeMillis();
        timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                            event -> clock.setText((new SimpleDateFormat("mm:ss"))
                                    .format(new Date(System.currentTimeMillis() - begin))
                            )
                ),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public Thread getSolver() {
        return solver;
    }
}
