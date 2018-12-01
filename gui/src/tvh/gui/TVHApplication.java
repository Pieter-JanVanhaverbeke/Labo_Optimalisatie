package tvh.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tvh.gui.controllers.TVHController;

import java.util.ArrayList;

public class TVHApplication extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tvh/views/tvh_view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("TVH - Solver");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (TVHController.solver != null) TVHController.solver.interrupt();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
