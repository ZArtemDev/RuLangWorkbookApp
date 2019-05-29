package com.ZArtemDev.RuLangWorkbookApp.student.task;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;

public class TaskController implements Initializable {
    String string = "           Дорогой _удрик!             \n " +
            "   Я х_чу рассказать тебе о своей ж_зни.\n" +
            "   Уч_ники старших кла__ов учстроили ля нас в школе живой уголок." +
            " _ам есть маленький ё_, черепаха, мышонок и черный у_." +
            " Все р_бята о них заботятся.\n" +
            "   Приезжай к нам!\n" ;
    String rightResult = "М_о_и_е_сс_Т_ж_ж_е_";

    Task task;
    LinkedList<Node> ll;



    @FXML
    BorderPane root;

    @FXML
    VBox vBox_task;

    @FXML
    Button button_next;

    @FXML
    ProgressBar progressBar_progress;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button_next.setOnAction(event -> nextTask());
        progressBar_progress.setPrefWidth(root.getPrefWidth());

        progressBar_progress.setProgress(0.0);

        ll = new LinkedList<>();
    }

    public void nextTask(){
        vBox_task.getChildren().clear();
        if(progressBar_progress.getProgress() < 1) {
            progressBar_progress.setProgress(progressBar_progress.getProgress()+0.1);

            if(!ll.isEmpty()) {
                if (task.getAnswer(ll).equals(rightResult)) {

                    System.out.println("task.getAnswer " + task.getAnswer(ll) + "\nrightResult " + rightResult + "\nRight!");
                } else {
                    System.out.println("task.getAnswer " + task.getAnswer(ll) + "\nrightResult " + rightResult + "\nWrong!");
                }
            }

            getTask();

            ll = task.getContentList();


            vBox_task.getChildren().addAll(task.coverIntoContainers(ll));
        }


    }

    public void getTask(){
        int type;
        Random random = new Random();
        type = random.nextInt(2);
        if(type == 0){
            System.out.println("InsertLetterTask");
            task = new InsertLetterTask(string, rightResult);
        }else  if(type == 1){
            System.out.println("TypeLetterTask");
            task = new TypeLetterTask(string, rightResult);
        }
    }
}
