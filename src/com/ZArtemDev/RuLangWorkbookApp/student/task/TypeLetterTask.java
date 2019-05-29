package com.ZArtemDev.RuLangWorkbookApp.student.task;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.awt.event.ActionEvent;
import java.util.LinkedList;

public class TypeLetterTask extends Task{
    private LinkedList<Node> contentList = null;
    private String  rightLettersSequence;

    public TypeLetterTask(String content, String  rightLettersSequence){
        contentList = new LinkedList<>();
        String[] splitedContent =  content.split("_");
        for(String s :splitedContent){
            if(!s.equals("")){
                contentList.add(new Text(s));
                contentList.add(new LimitedTextField());
            }else {
                contentList.remove(contentList.size()-1);
                contentList.add(new LimitedTextField(2));
            }
        }
        contentList.remove(contentList.size()-1);

    this.rightLettersSequence = rightLettersSequence;

    }

    public LinkedList<Node> getContentList(){
        return contentList;
    }

    public LinkedList coverIntoContainers(LinkedList ll){
        TextFlow textFlow = new TextFlow();
        textFlow.getChildren().addAll(ll);
        LinkedList nl = new LinkedList();
        nl.add(textFlow);
        return nl;
    }

    public String getAnswer(LinkedList ll){
        String answer = "";
        for(int i = 0; i < ll.size(); i++){
            if(ll.get(i) instanceof LimitedTextField){
                answer += ((LimitedTextField) ll.get(i)).getText() + "_";
            }
        }
        return answer;
    }
}
