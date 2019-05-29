package com.ZArtemDev.RuLangWorkbookApp.student.task;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.LinkedList;
import java.util.Random;

public class InsertLetterTask extends Task {

    private LinkedList<Node> contentList = null;
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
            target.setOnDragOver(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                    /* data is dragged over the target */
                    System.out.println("onDragOver");

                    /* accept it only if it is  not dragged from the same node
                     * and if it has a string data */
                    if (event.getGestureSource() != target &&
                            event.getDragboard().hasString()) {
                        /* allow for both copying and moving, whatever user chooses */
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }

                    event.consume();
                }
            });
            target.setOnDragEntered(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                    /* the drag-and-drop gesture entered the target */
                    System.out.println("onDragEntered");
                    /* show to the user that it is an actual gesture target */
                    if (event.getGestureSource() != target &&
                            event.getDragboard().hasString()) {
                        target.setFill(Color.GREEN);
                    }

                    event.consume();
                }
            });
            target.setOnDragExited(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                    /* mouse moved away, remove the graphical cues */
                    target.setFill(Color.BLACK);

                    event.consume();
                }
            });
            target.setOnDragDropped(new EventHandler <DragEvent>() {
                public void handle(DragEvent event) {
                    /* data dropped */
                    System.out.println("onDragDropped");
                    /* if there is a string data on dragboard, read it and use it */
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (db.hasString()) {
                        target.setText(db.getString());
                        success = true;
                    }
                    /* let the source know whether the string was successfully
                     * transferred and used */
                    event.setDropCompleted(success);

                    event.consume();
                }
            });

            if(!s.equals("")){
                contentList.add(new Text(s));
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
            lettersList.remove(pos);
            source.setOnDragDetected(new EventHandler <MouseEvent>() {
                public void handle(MouseEvent event) {
                    /* drag was detected, start drag-and-drop gesture*/
                    System.out.println("onDragDetected");

                    /* allow any transfer mode */
                    Dragboard db = source.startDragAndDrop(TransferMode.ANY);

                    /* put a string on dragboard */
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

        for(int i = 0; i < ll.size(); i++){
            if(contentList.get(i).getId()!= null && ll.get(i).getId().equals("target")){
                if(ll.get(i) instanceof Text){
                    answer += ((Text) ll.get(i)).getText() + "_";
                }
            }
        }
        return answer;
    }

    @Override
    public LinkedList coverIntoContainers(LinkedList ll) {
        TextFlow textFlow = new TextFlow();
        HBox hBox = new HBox();
        hBox.setSpacing(10.0);
        for(int i = 0; i < contentList.size(); i++){
            if(contentList.get(i).getId()!= null && contentList.get(i).getId().equals("target")){
                textFlow.getChildren().add(contentList.get(i));
            }else if(contentList.get(i).getId()!= null && contentList.get(i).getId().equals("source")){
                hBox.getChildren().add(contentList.get(i));
            }else {
                textFlow.getChildren().add(contentList.get(i));
            }
        }
        LinkedList nl = new LinkedList();
        nl.add(textFlow);
        nl.add(hBox);
        return nl;
    }

}
