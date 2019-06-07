package com.ZArtemDev.RuLangWorkbookApp.student.task;

import com.sun.javafx.geom.Curve;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class SentencePartsTask extends Task {
    private LinkedList<Node> contentList;
    private String  rightPartsSequence;

    public SentencePartsTask(String content, String  rightPartsSequence){
        this.rightPartsSequence = rightPartsSequence;
        contentList = new LinkedList<>();
        ArrayList<String> parts = new ArrayList<>();
        char[] charArray = content.toCharArray();
        for(int i = 0; i < charArray.length; i++){
            String word = "";
            if(Character.isAlphabetic(charArray[i])){
                word += charArray[i];
                if(i != charArray.length -1) {
                    while (Character.isAlphabetic(charArray[++i])) {
                        word += charArray[i];
                    }
                    i--;
                }
                parts.add(word);
            } else {
                word += charArray[i];
                if(i != charArray.length -1) {
                    while (!Character.isAlphabetic(charArray[++i])) {
                        word += charArray[i];
                    }
                    i--;
                }
                parts.add(word);
            }
        }
        for(String str : parts){
            Text text = new Text(str);
            if(Character.isAlphabetic(str.charAt(0))){
                VBox vBox = new VBox();
                vBox.setOnMouseClicked(event -> switchUnderline(vBox));
                vBox.setSpacing(1.0);
                vBox.getChildren().addAll(text);
                contentList.add(vBox);
            }else {
                contentList.add(text);
            }
        }
    }

    private ArrayList<Shape> getUnderline(Pane box){
        ArrayList<Shape> underlineList = new ArrayList<>();
        Line subject = new Line(0,0,box.getWidth(),0);
        underlineList.add(subject);
        Rectangle predicate = new Rectangle(0,0,box.getWidth(),box.getHeight());
        predicate.setArcHeight(0.0);
        underlineList.add(predicate);
        Line test = new Line(0,0,box.getWidth(),0);
        test.setStrokeDashOffset(1.0);
        underlineList.add(test);
        return underlineList;
    }

    private void switchUnderline(Pane box){
        ArrayList<Shape> underlineList = getUnderline(box);

        ListIterator<Shape> listIterator = underlineList.listIterator();
        if(listIterator.hasNext()){
            System.out.println(listIterator.next());
        }

        box.getChildren().add(listIterator.next());
    }

    @Override
    public LinkedList<Node> getContentList() {
        return contentList;
    }

    @Override
    public String getAnswer(LinkedList<Node> ll) {
        return "";
    }

    @Override
    public LinkedList coverIntoContainers(LinkedList ll) {
        TextFlow textFlow = new TextFlow();
        textFlow.getChildren().addAll(ll);
        LinkedList nl = new LinkedList();
        nl.add(textFlow);
        return nl;
    }
}
