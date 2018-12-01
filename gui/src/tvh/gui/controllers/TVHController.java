package tvh.gui.controllers;

import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class TVHController implements Initializable {

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
    @FXML private TabPane runningHeuristics;
    @FXML private SplitPane graphSplit;

    private Timeline timeline;
    private long begin;

    public static Thread solver;
    private XYChart.Series scores;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scoreGraph.setAnimated(false);
    }

    public void newHeuristic(Event mouseEvent) {
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
            ((HBox) mouseEvent.getSource()).setStyle("-fx-background-color: darkblue");
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
            ((HBox) mouseEvent.getSource()).setStyle("-fx-background-color: blue");
            ((HBox) mouseEvent.getSource()).setStyle("-fx-background-color: blue");
            NewHeuristicController newHeuristicController = NewHeuristicController.init();
            newHeuristicController.getStage().showAndWait();
            if (newHeuristicController.getProgramArguments() != null) {
                RunningHeuristicController controller = RunningHeuristicController.initView(
                        newHeuristicController.getProgramArguments(), newHeuristicController.getName());
                scoreGraph.getData().add(controller.getData());
                Tab tab = new Tab();
                tab.setText(newHeuristicController.getName());
                tab.setContent(controller.getNode());
                runningHeuristics.getTabs().add(tab);
            }
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_ENTERED) {
            ((HBox) mouseEvent.getSource()).setStyle("-fx-background-color: #0055FF");
        }
        else if (mouseEvent.getEventType() == MouseEvent.MOUSE_EXITED) {
            ((HBox) mouseEvent.getSource()).setStyle("-fx-background-color: blue");
        }
    }
}
