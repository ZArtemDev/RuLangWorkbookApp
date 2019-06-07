package com.ZArtemDev.RuLangWorkbookApp.student.task;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class InsertLetterTask extends Task {

    private LinkedList<Node> contentList;
    private String  rightLettersSequence;

    public InsertLetterTask(String content, String  rightLettersSequence){

        String[] letters = rightLettersSequence.split("_");
        LinkedList<String> lettersList = new LinkedList<>();
        for(int i = 0; i < letters.length; i++){
            lettersList.add(i, letters[i]);
        }
        Random random = new Random();

        contentList = new LinkedList<>();
        String[] splitedContent =  content.split("_");
        for(String s :splitedContent){
            Text target = new Text(40, 40, "_");
            target.setId("target");
            target.setStyle("-fx-font-size: 24");
            target.setOnDragOver(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                    System.out.println("onDragOver");
                    if (event.getGestureSource() != target &&
                            event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }

                    event.consume();
                }
            });
            target.setOnDragEntered(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                    System.out.println("onDragEntered");
                    if (event.getGestureSource() != target &&
                            event.getDragboard().hasString()) {
                        target.setFill(Color.GREEN);
                    }

                    event.consume();
                }
            });
            target.setOnDragExited(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                    target.setFill(Color.BLACK);

                    event.consume();
                }
            });
            target.setOnDragDropped(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                    /* data dropped */
                    System.out.println("onDragDropped");
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (db.hasString()) {
                        target.setText(db.getString());
                        success = true;
                    }
                    event.setDropCompleted(success);

                    event.consume();
                }
            });

            if(!s.equals("")){
                Text text = new Text(s);
                text.setStyle("-fx-font-size: 24");
                contentList.add(text);
                contentList.add(target);

            }else {
                contentList.remove(contentList.size()-1);
                contentList.add(target);
            }
        }

        contentList.remove(contentList.size()-1);

        while(!lettersList.isEmpty()){
            int pos = random.nextInt(lettersList.size());
            Text source = new Text(40, 40, lettersList.get(pos));
            source.setId("source");
            source.setStyle("-fx-font-size: 24");
            lettersList.remove(pos);
            source.setOnDragDetected(new EventHandler <MouseEvent>() {
                public void handle(MouseEvent event) {
                    System.out.println("onDragDetected");
                    Dragboard db = source.startDragAndDrop(TransferMode.ANY);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(source.getText());
                    db.setContent(content);

                    event.consume();
                }
            });
            source.setOnDragDone(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                    System.out.println("onDragDone");
                    if (event.getTransferMode() == TransferMode.MOVE) {
                        source.setText("");
                    }

                    event.consume();
                }
            });
            contentList.add(source);

        }
        this.rightLettersSequence = rightLettersSequence;
    }

    @Override
    public LinkedList<Node> getContentList() {
        return contentList;
    }

    @Override
    public String getAnswer(LinkedList<Node> ll) {
        String answer = "";

        for(Node node : ll){
            if(node.getId()!= null && node.getId().equals("target")){
                if(node instanceof Text){
                    answer += ((Text) node).getText() + "_";
                }
            }
        }

        if(answer.equals(rightLettersSequence)){
            System.out.println("task.getAnswer " + answer + "\nrightResult " + rightLettersSequence + "\nRight!");
            return "correct";
        }
        System.out.println("task.getAnswer " + answer + "\nrightResult " + rightLettersSequence + "\nWrong!");
        return "incorrect " + answer;
    }

    @Override
    public LinkedList coverIntoContainers(LinkedList<Node> ll) {
        TextFlow textFlow = new TextFlow();
        textFlow.setId("contentPane");
        HBox hBox = new HBox();
        hBox.setId("lettersPane");
        hBox.setSpacing(10.0);

        for (Node node: contentList) {
            if(node.getId() != null && node.getId().equals("target")){
                textFlow.getChildren().add(node);
            }else if(node.getId()!= null && node.getId().equals("source")){
                hBox.getChildren().add(node);
            }else {
                textFlow.getChildren().add(node);
            }
        }

        LinkedList<Node> nl = new LinkedList<>();
        nl.add(textFlow);
        nl.add(hBox);
        return nl;
    }



}
