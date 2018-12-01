package tvh.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewHeuristicController implements Initializable{

    @FXML private TextField name;
    @FXML private TextArea programOptions;
    private Stage stage;

    private String result = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public static NewHeuristicController init() {

        NewHeuristicController controller = null;
        try {
            FXMLLoader loader = new FXMLLoader(NewHeuristicController.class.getResource(
                    "/tvh/views/new_heuristic.fxml"
            ));
            Scene scene = new Scene(loader.load());
            controller = loader.getController();
            controller.stage = new Stage();
            controller.stage.setScene(scene);
            controller.stage.initModality(Modality.APPLICATION_MODAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return controller;
    }

    public void newHeuristic(MouseEvent mouseEvent) {
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
            ((HBox) mouseEvent.getSource()).setStyle("-fx-background-color: darkblue");
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
            ((HBox) mouseEvent.getSource()).setStyle("-fx-background-color: blue");
            result = programOptions.getText();
            stage.close();
        } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_ENTERED) {
            ((HBox) mouseEvent.getSource()).setStyle("-fx-background-color: #0055FF");
        }
        else if (mouseEvent.getEventType() == MouseEvent.MOUSE_EXITED) {
            ((HBox) mouseEvent.getSource()).setStyle("-fx-background-color: blue");
        }
    }

    public Stage getStage() {
        return stage;
    }

    public String getProgramArguments() {
        return result;
    }

    public String getName() {
        return name.getText();
    }
}
