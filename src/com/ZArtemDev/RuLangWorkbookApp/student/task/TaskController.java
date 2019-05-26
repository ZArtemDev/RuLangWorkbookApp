package com.ZArtemDev.RuLangWorkbookApp.student.task;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskController implements Initializable {

    @FXML
    TextFlow textFlow;

    @FXML
    Button btn;

    private ObservableList list;

    public void getResult(){
        String rightResult = "e";
        String s = "";
        for(int i = 0; i < textFlow.getChildren().size(); i++){
            if(textFlow.getChildren().get(i) instanceof TextField){
                s += ((TextField) textFlow.getChildren().get(i)).getText();
            }
        }
        if(s.equals(rightResult)){
            System.out.println("That's right");
        }else {
            System.out.println("That's wrong");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String string = "           Дорогой _удрик!             \n " +
                        "   Я х_чу рассказать тебе о своей ж_зни.\n" +
                        "   Уч_ники старших кла__ов учстроили ля нас в школе живой уголок." +
                        " _ам есть маленький ё_, черепаха, мышонок и черный у_." +
                        " Все р_бята о них заботятся.\n" +
                        "   Приезжай к нам!" ;

        String[] strings = string.split("_");
        list = textFlow.getChildren();

        LimitedTextField textField = new LimitedTextField();
        textField.setPrefColumnCount(1);

        for(String s : strings ){
            System.out.println(s);
            if(!s.equals("")) {
                list.add(new Text(s));
                list.add(new LimitedTextField());
            } else {
                list.remove(list.size()-1);
                list.add(new LimitedTextField(2));
            }
        }
        list.remove(list.size()-1);
        Button btn = new Button("Result");

    }
}
