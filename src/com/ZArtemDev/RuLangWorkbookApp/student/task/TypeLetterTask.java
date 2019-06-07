package com.ZArtemDev.RuLangWorkbookApp.student.task;

import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.LinkedList;

public class TypeLetterTask extends Task{
    private final String textFieldStyle =   "-fx-font-size: 24;"
                                    + "-fx-background-color: transparent; "
                                    + "-fx-border-width: 0 0 1 0;"
                                    + "-fx-border-color: black;"
                                    + "-fx-padding: -4;"
                                    + "-fx-alignment: center;"
                                    + "fx-background-insets:  0, 0 0 1 0;"
                                    + "-fx-background-radius: 0";

    private LinkedList<Node> contentList;
    private String  rightLettersSequence;

    public TypeLetterTask(String content, String  rightLettersSequence){
        this.contentList = new LinkedList<>();
        String[] splitedContent =  content.split("_");
        for(String s :splitedContent){
            if(!s.equals("")){
                Text text = new Text(s);
                text.setStyle("-fx-font-size: 24");
                contentList.add(text);
                LimitedTextField limitedTextField = new LimitedTextField();
                limitedTextField.setStyle(textFieldStyle);

                contentList.add(limitedTextField);
            }else {
                contentList.remove(contentList.size()-1);
                LimitedTextField limitedTextField = new LimitedTextField(2);
                limitedTextField.setStyle(textFieldStyle);
                contentList.add(limitedTextField);
            }
        }
        contentList.remove(contentList.size()-1);

        this.rightLettersSequence = rightLettersSequence;
    }

    public LinkedList<Node> getContentList(){
        return contentList;
    }

    public String getAnswer(LinkedList<Node> ll){
        String answer = "";

        for (Node node : ll) {
            if(node instanceof LimitedTextField){
                answer += ((LimitedTextField) node).getText() + "_";
            }
        }

        if(answer.equals(rightLettersSequence)){
            System.out.println("task.getAnswer " + answer + "\nrightResult " + rightLettersSequence + "\nRight!");
            return "correct";
        }
        System.out.println("task.getAnswer " + answer + "\nrightResult " + rightLettersSequence + "\nWrong!");
        return "incorrect " + answer;
    }

    public LinkedList coverIntoContainers(LinkedList<Node> ll){
        TextFlow textFlow = new TextFlow();
        textFlow.setId("contentPane");
        textFlow.getChildren().addAll(ll);
        LinkedList<Node> nl = new LinkedList<>();
        nl.add(textFlow);
        return nl;
    }
}
