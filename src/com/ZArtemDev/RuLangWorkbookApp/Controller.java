package com.ZArtemDev.RuLangWorkbookApp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private DBConnector dbConnector = null;
    private Connection connection = null;

    private final int ADMINISTRATOR = 1;
    private final int TEACHER = 2;
    private final int STUDENT = 3;


    @FXML
    TextField userNameField;

    @FXML
    TextField passwordField;

    @FXML
    Button loginBtn;

    @FXML
    Label errorInfoLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.dbConnector = DBConnector.getInstance();
        this.connection = dbConnector.getConnection();

    }

    public void createNewStage(ActionEvent event, String fxml, String title) {
        try {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        newStage.setTitle(title);
        newStage.setScene(new Scene(root, 1024, 576));
        newStage.show();
        }catch (Exception ex) {
            System.out.println("ERROR new window");
            ex.printStackTrace();
        }
    }


    public void login(ActionEvent event){
        try {
            if(isValidUser(userNameField.getText(), passwordField.getText())) {
                Statement st = this.connection.createStatement();
                ResultSet rs = st.executeQuery("select * from rulangdatabase.users where username = '" + userNameField.getText() + "';");
                while (rs.next()) {
                    dbConnector.setActiveUser(userNameField.getText());
                    if (rs.getInt("type") == ADMINISTRATOR) {
                        StageLoader.createNewStage(event, "admin.fxml", "Administrator page");
                    } else if (rs.getInt("type") == TEACHER) {
                        StageLoader.createNewStage(event, "teacher.fxml", "Teacher page");
                    } else if (rs.getInt("type") == STUDENT) {
                        StageLoader.createNewStage(event, "student.fxml", "Student page");
                    } else {
                        System.out.println("Incorrect usertype in database");
                    }
                }
                st.close();
            }
        }catch (SQLException ex) {
            System.out.println("ERROR connection");
            ex.printStackTrace();
        }
    }

    public boolean isValidUser(String username, String password) {
        try {
            String query = "SELECT * FROM rulangdatabase.users WHERE username = '" + username + "' and password = '" + password + "'";

            System.out.println(query);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next()){ return true;};
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        errorInfoLabel.setText("Неправильное имя или пароль");
        return false;
    }


}
