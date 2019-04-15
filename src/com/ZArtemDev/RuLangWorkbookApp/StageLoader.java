package com.ZArtemDev.RuLangWorkbookApp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StageLoader {
    private static double xOffset = 0.0;
    private static double yOffset = 0.0;

    public static void createFirstStage(Stage primaryStage, String fxml, String title){
        try {
            Parent root = FXMLLoader.load(StageLoader.class.getResource(fxml));
            primaryStage.initStyle(StageStyle.UNDECORATED);
            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    primaryStage.setX(event.getScreenX() - xOffset);
                    primaryStage.setY(event.getScreenY() - yOffset);
                }
            });

            primaryStage.setTitle(title);
            primaryStage.setScene(new Scene(root, 1024, 576));
            primaryStage.show();
        }catch (Exception ex) {
            System.out.println("ERROR new window");
            ex.printStackTrace();
        }
    }

    public static void createNewStage(ActionEvent event, String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(StageLoader.class.getResource(fxml));
            Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    newStage.setX(event.getScreenX() - xOffset);
                    newStage.setY(event.getScreenY() - yOffset);
                }
            });

            newStage.setTitle(title);
            newStage.setScene(new Scene(root, 1024, 576));
            newStage.show();
        }catch (Exception ex) {
            System.out.println("ERROR new window");
            ex.printStackTrace();
        }
    }
}
