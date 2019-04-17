package com.ZArtemDev.RuLangWorkbookApp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    DBConnector dbConnector = null;
    private Connection connection = null;

    @FXML
    Label userNameLabel;

    @FXML
    Button exitBtn;

    @FXML
    Button addUserBtn;

    @FXML
    Button removeUserBtn;

    @FXML
    VBox mainVBox;

    @FXML
    VBox addingUserPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.dbConnector = DBConnector.getInstance();
        this.connection = dbConnector.getConnection();
        System.out.println("2Username: " + dbConnector.getActiveUser());
        userNameLabel.setText(dbConnector.getActiveUser());
        try {
            addingUserPane = FXMLLoader.load(getClass().getResource("addingUserPane.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void exit(ActionEvent event){
        this.dbConnector.setActiveUser("");
        StageLoader.createNewStage(event, "logger.fxml","RuLang Application" );

    }

    public void createAddPane(ActionEvent event){
            if(!mainVBox.getChildren().contains(addingUserPane))
                mainVBox.getChildren().add(addingUserPane);
    }
}
