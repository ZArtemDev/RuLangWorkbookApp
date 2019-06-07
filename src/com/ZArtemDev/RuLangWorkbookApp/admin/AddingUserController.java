package com.ZArtemDev.RuLangWorkbookApp.admin;

import com.ZArtemDev.RuLangWorkbookApp.utilities.DBConnector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddingUserController implements Initializable {
    private String login, password;

    @FXML VBox root;

    @FXML GridPane gridPane_main_info, gridPane_add_info, gridPane_gen_info, gridPane_add_subject, gridPane_add_class;

    @FXML Button button_generate, button_add_new_user;

    @FXML Label label_generated_name_and_pass;

    @FXML TextField textField_last_name, textField_first_name, textField_middle_name;

    @FXML ComboBox<String> comboBox_location, comboBox_school, comboBox_subject, comboBox_class;

    @FXML ChoiceBox<String> choiceBox_user_type;

    Connection connection = DBConnector.getInstance().getConnection();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.getChildren().removeAll(gridPane_main_info, gridPane_add_info, gridPane_add_subject, gridPane_add_class, gridPane_gen_info);
        choiceBox_user_type = new ChoiceBox<>(FXCollections.observableArrayList("Администратор", "Учитель", "Ученик"));
        choiceBox_user_type.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root.getChildren().removeAll(gridPane_main_info, gridPane_add_info, gridPane_add_subject, gridPane_add_class, gridPane_gen_info);
                switch (choiceBox_user_type.getValue()) {
                    case "Администратор":
                        root.getChildren().addAll(gridPane_main_info, gridPane_gen_info);
                        break;
                    case "Учитель":
                        root.getChildren().addAll(gridPane_main_info, gridPane_add_info, gridPane_add_subject , gridPane_gen_info);
                        break;
                    case "Ученик":
                        root.getChildren().addAll(gridPane_main_info, gridPane_add_info, gridPane_add_class, gridPane_gen_info);
                        break;
                    default:
                        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).logp(Level.WARNING,
                                "AddingUserController.java",
                                "initialize(URL location, ResourceBundle resources)",
                                "choiceBox has incorrect value");
                }
            }
        });
        root.getChildren().add(choiceBox_user_type);
        choiceBox_user_type.valueProperty().setValue("Администратор");
        button_generate.setOnAction(event -> this.generateNameAndPass());
        button_add_new_user.setOnAction(event -> this.addNewUser(choiceBox_user_type.getValue()));
        setAddInfo();
    }

    private void setAddInfo(){
        String query = "select distinct(location) from rulangdatabase.locations order by location asc";
        comboBox_location.setItems(getList(connection, query));
        comboBox_location.setEditable(true);
        comboBox_location.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String query = "select distinct(school_name) FROM rulangdatabase.schools s "
                        + "INNER JOIN rulangdatabase.locations l on s.location_id = l.id where location = '"
                        + comboBox_location.getValue()
                        + "' order by school_name asc;";
                comboBox_school.setItems(getList(connection, query));
                comboBox_school.valueProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        String query = "select distinct(class_name) FROM rulangdatabase.classes c "
                                + "INNER JOIN rulangdatabase.schools s on c.school_id = s.school_id where school_name ='"
                                + comboBox_school.getValue()
                                +"' order by class_name asc";
                        comboBox_class.setItems(getList(connection, query));

                    }
                });
            }
        });
        comboBox_subject.setItems(FXCollections.observableArrayList("Русский язык"));
        comboBox_subject.valueProperty().setValue("Русский язык");
    }

    private void addNewUser(String userType){
        String query = "";
        switch (userType){
            case "Администратор":
                query = "insert into rulangdatabase.administrators(login, password, first_name, last_name, middle_name) values('"
                        + login + "','" + password + "','" + textField_first_name.getText() + "','" + textField_last_name.getText()
                        + "','" + textField_middle_name.getText() + "')";
                break;
            case "Учитель":
                query = "insert into rulangdatabase.teachers(login, password, first_name, last_name, middle_name, school_id) values('"
                        + login + "','" + password + "','" + textField_first_name.getText() + "','" + textField_last_name.getText()
                        + "','" + textField_middle_name.getText() + "',(select school_id from rulangdatabase.schools where school_name = '"
                        + comboBox_school.getValue() + "'))";
                break;
            case "Ученик":
                query = "insert into rulangdatabase.students(login, password, first_name, last_name, middle_name, class_id) values('"
                        + login + "','" + password + "','" + textField_first_name.getText() + "','" + textField_last_name.getText()
                        + "','" + textField_middle_name.getText() + "',(select class_id from rulangdatabase.classes where class_name = '"
                        + comboBox_class.getValue() + "'))";
                break;
        }
        try {
            System.out.println(query);
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            st.close();
        } catch (SQLException e) {
            root.getChildren().add(new Label("Ошибка при добавлении пользователя"));
            e.printStackTrace();
        }
    }

    public void generateNameAndPass(){
        String name = genName();
        String pass = genPass();
        login = name;
        password = pass;
        label_generated_name_and_pass.setText("Логин: " + name + " Пароль: " + pass);
    }


    private String genName(){
        String name = "";
        String query = "";
        switch (choiceBox_user_type.getValue()) {
            case "Администратор":
                name += "ad_";
                query = "select admin_id from rulangdatabase.administrators order by admin_id desc";
                break;
            case "Учитель":
                name += "th_";
                query = "select teacher_id from rulangdatabase.teachers order by teacher_id desc";
                break;
            case "Ученик":
                name += "st_";
                query = "select student_id from rulangdatabase.students order by student_id desc";
                break;
            default:
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).logp(Level.WARNING,
                        "AddingUserController.java",
                        "genName()",
                        "choiceBox has incorrect value");
        }
        System.out.println(query);
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next()){
                String number = rs.getString(1);
                while(name.length() < 12 - number.length()){
                    name += "0";
                }
                name += number;
            } else {
                name += "000000000";
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String genPass(){
        Random random = new Random();

        short passLength = 12;
        char[] pass = new char[passLength];

        for(short i = 0; i < passLength; i++){
            pass[i] = (char) (random.nextInt(93) + '!'); // keyboard symbols
            if(pass[i] == '\''){
                pass[i] = 'c';
            }
        }

        return String.valueOf(pass);
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
