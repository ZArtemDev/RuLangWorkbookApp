package com.ZArtemDev.RuLangWorkbookApp;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;

public class AddingUserController implements Initializable {
    @FXML
    ChoiceBox typeChoiceBox;

    @FXML
    Label generatedNameAndPass;

    @FXML
    TextField firstName;

    @FXML
    TextField lastName;

    @FXML
    Button genBtn;

    @FXML
    VBox root;

    @FXML
    GridPane teachersAdditionalInfo;

    @FXML
    GridPane studentsAdditionalInfo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            teachersAdditionalInfo = FXMLLoader.load(getClass().getResource("teachersAdditionalInfo.fxml"));
            studentsAdditionalInfo = FXMLLoader.load(getClass().getResource("studentsAdditionalInfo.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        typeChoiceBox.setItems(FXCollections.observableArrayList("Администратор", "Учитель", "Ученик"));
        typeChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(root.getChildren().contains(teachersAdditionalInfo))
                    root.getChildren().remove(teachersAdditionalInfo);
                if(root.getChildren().contains(studentsAdditionalInfo))
                    root.getChildren().remove(studentsAdditionalInfo);
                    switch (typeChoiceBox.getValue().toString()) {
                        case "Администратор":
                            break;
                        case "Учитель":
                            root.getChildren().add(teachersAdditionalInfo);
                            break;
                        case "Ученик":
                            root.getChildren().add(studentsAdditionalInfo);
                            break;
                        default:
                    }

            }
        });
        genBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                generatedNameAndPass.setText(genPass());
            }
        });

    }

    private String genName(){
        String name= "";
        return name;
    }

    private String genPass(){
        Random random = new Random();

        short passLength = 12;
        char[] pass = new char[passLength];

        for(short i = 0; i < passLength; i++){
            pass[i] = (char) (random.nextInt(93) + '!'); // keyboard symbols
        }

        return String.valueOf(pass);
    }


}
