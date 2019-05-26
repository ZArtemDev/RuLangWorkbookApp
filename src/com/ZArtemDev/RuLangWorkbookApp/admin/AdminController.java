package com.ZArtemDev.RuLangWorkbookApp.admin;

import com.ZArtemDev.RuLangWorkbookApp.utilities.DBConnector;
import com.ZArtemDev.RuLangWorkbookApp.StageLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    DBConnector dbConnector = null;
    private Connection connection = null;

    @FXML
    Label label_username;

    @FXML
    Button button_exit;

    @FXML
    Button addUserBtn, removeUserBtn, addSchoolBtn, tuneSchoolBtn;

    @FXML
    AnchorPane mainBox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.dbConnector = DBConnector.getInstance();
        this.connection = dbConnector.getConnection();
        System.out.println("2Username: " + dbConnector.getActiveUser());
        label_username.setText(dbConnector.getActiveUser());

        createPane("adding_user_pane.fxml");

        addUserBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createPane("adding_user_pane.fxml");
            }
        });
        addSchoolBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createPane("adding_school_pane.fxml");
            }
        });
        tuneSchoolBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { createPane("tunning_school_pane.fxml");}
        });


    }

    public void exit(ActionEvent event){
        this.dbConnector.setActiveUser("");
        StageLoader.createNewStage(event, "logger.fxml","RuLang Application" );
    }

    public void createPane(String fxml){
        if(!mainBox.getChildren().isEmpty()){
            mainBox.getChildren().clear();
        }
        try {
            Pane pane = FXMLLoader.load(getClass().getResource(fxml));
            mainBox.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
