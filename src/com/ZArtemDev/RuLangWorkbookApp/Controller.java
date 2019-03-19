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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        DBConnector dbConnector = DBConnector.getInstance();
        this.connection = dbConnector.getConnection();

    }

    public void createNewStage(ActionEvent event, String fxml) {
        try {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        newStage.setScene(new Scene(root, 1200, 675));
        newStage.show();
        }catch (Exception ex) {
            System.out.println("ERROR new window");
            ex.printStackTrace();
        }
    }


    public void login(ActionEvent event){
        try {
            System.out.println("Clicked");
            System.out.println("Username: " + userNameField.getText());
            if(isValidUser(userNameField.getText(), passwordField.getText())) {
                Statement st = this.connection.createStatement();
                ResultSet rs = st.executeQuery("select * from rulangdatabase.users where username = '" + userNameField.getText() + "';");
                while (rs.next()) {

                    if (rs.getInt("type") == ADMINISTRATOR) {
                        createNewStage(event, "admin.fxml");
                    } else if (rs.getInt("type") == TEACHER) {
                        createNewStage(event, "teacher.fxml");
                    } else if (rs.getInt("type") == STUDENT) {
                        createNewStage(event, "student.fxml");
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
        System.out.println("Authorization error");
        return false;
    }


}
