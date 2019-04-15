package com.ZArtemDev.RuLangWorkbookApp;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        typeChoiceBox.setItems(FXCollections.observableArrayList("Администратор", "Учитель", "Ученик"));
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
