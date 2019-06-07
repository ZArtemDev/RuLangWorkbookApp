package com.ZArtemDev.RuLangWorkbookApp.admin;

import com.ZArtemDev.RuLangWorkbookApp.utilities.DBConnector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class TunningSchoolPaneController implements Initializable {
    private short school_id;

    Connection connection = DBConnector.getInstance().getConnection();

    @FXML
    AnchorPane root;

    @FXML
    GridPane gridPane_select_school;

    @FXML
    FlowPane flowPane_classes;

    @FXML
    ComboBox<String> comboBox_region, comboBox_district, comboBox_location, comboBox_school;

    @FXML Button button_add_class;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String query = "select distinct(federal_district) from rulangdatabase.locations order by federal_district asc"; //Область
        comboBox_region.setItems(getList(connection, query));
        comboBox_region.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String query = "select distinct(region) from rulangdatabase.locations where federal_district = '"
                        + comboBox_region.getValue() + "' order by region asc"; //Район
                comboBox_district.setItems(getList(connection, query));
                comboBox_district.valueProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        String query = "select distinct(location) from rulangdatabase.locations where federal_district = '"
                                + comboBox_region.getValue() + "' and region = '" + comboBox_district.getValue()
                                + "' order by location asc"; //Город
                        comboBox_location.setItems(getList(connection, query));
                        comboBox_location.valueProperty().addListener(new ChangeListener<String>() {
                            @Override
                                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                                String query = "select distinct(school_name) from rulangdatabase.schools s inner join "
                                        +"rulangdatabase.locations l on s.location_id = l.id where location = '"
                                        + comboBox_location.getValue()
                                        + "' order by school_name asc"; //Школа
                                comboBox_school.setItems(getList(connection, query));
                                comboBox_school.valueProperty().addListener(new ChangeListener<String>() {
                                    @Override
                                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                        String query = "select school_id from rulangdatabase.schools where school_name ='"
                                                + comboBox_school.getValue() + "'";
                                        try {
                                            Statement st = connection.createStatement();
                                            ResultSet rs = st.executeQuery(query);
                                            while(rs.next()){
                                                school_id = rs.getShort("school_id");
                                            }
                                            st.close();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                        root.getChildren().remove(gridPane_select_school);
                                        root.getChildren().add(new Label(comboBox_school.getValue()));
                                        getClasses();
                                    }
                                });
                            }
                        });
                    }
                });

            }
        });

    }

    private void getClasses(){
        flowPane_classes.getChildren().clear();
        String query = "select class_name from rulangdatabase.classes where school_id = '" + school_id + "'";
        ObservableList<String> classes = getList(connection, query);
        if(!classes.isEmpty()) {
            for (short i = 0; i < classes.size(); i++) {
                flowPane_classes.getChildren().add(new Label(classes.get(i)));
            }
        }
        button_add_class = new Button("Добавить класс");
        button_add_class.setOnAction(event -> addClass(event));
        flowPane_classes.getChildren().add(button_add_class);
    }

    private void addClass(ActionEvent event){
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.TOP_CENTER);
        dialogVbox.setStyle("-fx-background-color: #f39c12;"
                            + "-fx-border-color: gray");
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.TOP_RIGHT);
        Button button_exit = new Button();
        button_exit.setPrefSize(30, 30);
        button_exit.setStyle("-fx-background-color: transparent;"
                + "-fx-background-image: url('com/ZArtemDev/RuLangWorkbookApp/resources/close_icon.png');"
                + "-fx-margin: 0 0 0 20");
        button_exit.setOnAction(event1 -> dialog.close());
        hBox.getChildren().add(button_exit);
        dialogVbox.getChildren().add(hBox);

        dialogVbox.getChildren().add(new Text("Добавить класс"));
        TextField textField_class = new TextField();
        dialogVbox.getChildren().add(textField_class);
        Button button_add = new Button("Добавить");
        button_add.setOnAction(event1 -> getValue(dialog, textField_class));
        dialogVbox.getChildren().add(button_add);
        Scene dialogScene = new Scene(dialogVbox, 200, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void getValue(Stage gialog, TextField textField_class){
        String query = "select class_name from rulangdatabase.classes where school_id = '" + school_id + "'";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                if(rs.getString(1).equals(textField_class.getText())){
                    gialog.close();
                }
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query = "insert into rulangdatabase.classes(class_name, school_id) values ('" + textField_class.getText() + "' , '" + 11 + "');";
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        flowPane_classes.getChildren().remove(button_add_class);
        flowPane_classes.getChildren().add(new Label(textField_class.getText()));
        flowPane_classes.getChildren().add(button_add_class);
        gialog.close();
    }


    private ObservableList<String> getList(Connection connection, String query){
        ObservableList<String> obj = FXCollections.observableArrayList();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                obj.add(rs.getString(1));
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
