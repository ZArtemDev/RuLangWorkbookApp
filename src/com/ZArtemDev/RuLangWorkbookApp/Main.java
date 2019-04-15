package com.ZArtemDev.RuLangWorkbookApp;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private double xOffset = 0.0;
    private double yOffset = 0.0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        StageLoader.createFirstStage(primaryStage, "logger.fxml", "RuLang Application");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DBConnector.getInstance().closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
