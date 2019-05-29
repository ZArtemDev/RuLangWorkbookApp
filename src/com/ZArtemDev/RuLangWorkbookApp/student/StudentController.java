package com.ZArtemDev.RuLangWorkbookApp.student;

import com.ZArtemDev.RuLangWorkbookApp.StageLoader;
import com.ZArtemDev.RuLangWorkbookApp.utilities.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class StudentController implements Initializable {
    DBConnector dbConnector = null;
    private Connection connection = null;

    @FXML
    Label label_username;

    @FXML
    Button button_exit;

    @FXML
    BorderPane root, borderPane_task;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.dbConnector = DBConnector.getInstance();
        this.connection = dbConnector.getConnection();
        label_username.setText(dbConnector.getActiveUser());

        try {
            borderPane_task.setPrefWidth(root.getPrefWidth());
            borderPane_task = FXMLLoader.load(getClass().getResource("task/task.fxml"));
            //root.setCenter(FXMLLoader.load(getClass().getResource("task/task.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit(ActionEvent event){
        this.dbConnector.setActiveUser("");
        StageLoader.createNewStage(event, "logger.fxml","RuLang Application" );
    }
}
