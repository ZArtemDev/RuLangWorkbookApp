package com.ZArtemDev.RuLangWorkbookApp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private DBConnector dbConnector = new DBConnector();

    @FXML
    Label sqlIndex;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sqlIndex.setText(dbConnector.getDatabases());
    }
}
