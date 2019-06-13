package com.ZArtemDev.RuLangWorkbookApp.admin;

import com.ZArtemDev.RuLangWorkbookApp.utilities.DBConnector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class RemovingUserController implements Initializable {

    Connection connection = DBConnector.getInstance().getConnection();

    @FXML
    ComboBox<String> comboBox_region, comboBox_district, comboBox_location, comboBox_school, comboBox_class;

    @FXML
    VBox vBox_users;

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
                                        String query = "select distinct(class_name) from rulangdatabase.classes c inner join "
                                                +"rulangdatabase.schools s on s.school_id = c.school_id where school_name = '"
                                                + comboBox_school.getValue()
                                                + "' order by school_name asc"; //Класс
                                        comboBox_class.setItems(getList(connection, query));
                                        comboBox_class.valueProperty().addListener(new ChangeListener<String>() {
                                            @Override
                                            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                                    String user;
                                                    String query = "select student_id, first_name, last_name, middle_name from rulangdatabase.students s inner join " +
                                                            "rulangdatabase.classes c on s.class_id = c.class_id  where class_name ='"
                                                            + comboBox_class.getValue() + "'";
                                                    try {
                                                        Statement st = connection.createStatement();
                                                        ResultSet rs = st.executeQuery(query);
                                                        while(rs.next()){
                                                            user = rs.getString("last_name") + rs.getString("first_name")
                                                                    + rs.getString("middle_name");
                                                            String user_id = rs.getString("student_id");
                                                            Button button_user = new Button(user);
                                                            button_user.getStyleClass().add("button_user");
                                                            ImageView imageView = new ImageView("/com/ZArtemDev/RuLangWorkbookApp/resources/remove_icon.png");
                                                            button_user.setGraphic(imageView);
                                                            button_user.setContentDisplay(ContentDisplay.RIGHT);
                                                            button_user.alignmentProperty().setValue(Pos.CENTER_RIGHT);
                                                            button_user.setOnAction(event -> removeUser(button_user,user_id));
                                                            vBox_users.getChildren().add(button_user);
                                                        }
                                                        st.close();
                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    }


                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });

            }
        });
    }

    private void removeUser(Button button, String user_id){
        String query = "DELETE FROM rulangdatabase.students WHERE (student_id = '" + user_id+ "')";
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        vBox_users.getChildren().remove(button);
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
