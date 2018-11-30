package tvh.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tvh.gui.controllers.TVHController;

public class TVHApplication extends Application{

    private TVHController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tvh/views/tvh_view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("TVH - Solver");
        primaryStage.setScene(scene);
        controller = loader.getController();
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (controller.getSolver() != null) controller.getSolver().interrupt();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
