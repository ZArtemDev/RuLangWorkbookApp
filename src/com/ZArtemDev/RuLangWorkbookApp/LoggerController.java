package com.ZArtemDev.RuLangWorkbookApp;

import com.ZArtemDev.RuLangWorkbookApp.utilities.DBConnector;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerController  implements Initializable {

    private DBConnector dbConnector = null;
    private Connection connection = null;
    private Logger log = null;
    private String type = null;
    private String user_info = null;

    @FXML TextField userNameField;
    @FXML TextField passwordField;
    @FXML Button loginBtn;
    @FXML Button exitBtn;
    @FXML Label errorInfoLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.dbConnector = DBConnector.getInstance();
        this.connection = dbConnector.getConnection();
        log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        exitBtn.setId("exitBtn");
        exitBtn.setOnAction(event -> Platform.exit());

        userNameField.setText("thtest");
        passwordField.setText("test");
    }

    public void login(ActionEvent event){
            if(isValidUser(userNameField.getText(), passwordField.getText())) {
                dbConnector.setActiveUser(user_info);
                if (type.equals("ad")) {
                    StageLoader.createNewStage(event,"admin/admin.fxml", null, "Administrator page");
                } else if (type.equals("th")) {
                    StageLoader.createNewStage(event, "teacher/teacher.fxml", null, "Teacher page");
                } else if (type.equals("st")) {
                    StageLoader.createNewStage(event,"student/student.fxml", "student/student.css", "Student page");
                } else {
                    log.logp(Level.SEVERE, "LoggerController", "login", "Incorrect user type in database");
                }
            }
    }

    private boolean isValidUser(String username, String password) {
        type = username.substring(0, 2);
        String query = "";
        switch (type){
            case "ad":
                query ="select * from rulangdatabase.administrators where login ='"
                        + username + "' and password = '" + password + "'";
                break;
            case "th":
                query ="select * from rulangdatabase.teachers where login ='"
                        + username + "' and password = '" + password + "'";
                break;
            case "st":
                query ="select * from rulangdatabase.students where login ='"
                        + username + "' and password = '" + password + "'";
                break;
        }
        try {
            log.logp(Level.INFO, "LoggerController", "isValidUser", query);

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next()){
                user_info = rs.getString("login");
                System.out.println(user_info);
                return true;
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        errorInfoLabel.setText("Неправильное имя или пароль");
        return false;
    }
}
