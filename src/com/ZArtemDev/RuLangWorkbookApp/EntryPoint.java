package com.ZArtemDev.RuLangWorkbookApp;

import com.ZArtemDev.RuLangWorkbookApp.utilities.DBConnector;
import javafx.application.Application;
import javafx.stage.Stage;

public class EntryPoint extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        StageLoader.createFirstStage(primaryStage, "student/task/task.fxml", "RuLang Application");
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
