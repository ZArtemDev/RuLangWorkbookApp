package com.ZArtemDev.RuLangWorkbookApp.teacher;

import com.ZArtemDev.RuLangWorkbookApp.LoggerController;
import com.ZArtemDev.RuLangWorkbookApp.StageLoader;
import com.ZArtemDev.RuLangWorkbookApp.utilities.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TeacherController implements Initializable {
    DBConnector dbConnector = null;
    private Connection connection = null;

    @FXML
    BorderPane root;

    @FXML
    VBox vBox_courses, vBox_that_course;

    @FXML
    AnchorPane anchorPane_courses;

    @FXML
    Label label_username;

    @FXML
    Button button_exit, button_course, button_add_course;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.dbConnector = DBConnector.getInstance();
        this.connection = dbConnector.getConnection();
        initUser(dbConnector.getActiveUser());

        loadVBoxCourses();

    }

    public void initUser(String user){
        String query = "select * from rulangdatabase.teachers where login = '" + user + "'";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next())
                label_username.setText(rs.getString("last_name") + " " + rs.getString("first_name") + " " + rs.getString("middle_name"));
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadVBoxCourses(){
        vBox_courses.getChildren().clear();
        String query = "select * from rulangdatabase.courses c\n" +
                "inner join rulangdatabase.teachers t \n" +
                "on c.teacher = t.teacher_id \n" +
                "inner join rulangdatabase.classes cl\n" +
                "on cl.class_id = c.class \n" +
                "where login = '"
                + dbConnector.getActiveUser() + "'";
        System.out.println(query);
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                button_course = new Button();
                button_course.getStyleClass().add("button_course");
                button_course.setText(rs.getString("subject") + "\n\n" + "Класс " + rs.getString("class_name"));
                int course_id = rs.getInt("course_id");
                button_course.setOnAction(event -> openCourseInfo(course_id));
                vBox_courses.getChildren().add(button_course);
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        button_add_course = new Button();
        button_add_course.setId("button_add_course");
        button_add_course.setText("Добавить новый курс");
        button_add_course.setOnAction(event -> createCourse());
        vBox_courses.getChildren().add(button_add_course);
    }

    private void openCourseInfo(int course_id){

        anchorPane_courses.getChildren().clear();
        vBox_that_course = new VBox();
        vBox_that_course.setId("vBox_that_course");

        vBox_that_course.getChildren().add(new Label("Информация о курсе"));
        vBox_that_course.getChildren().add(new Separator());

        ArrayList<String> students = new ArrayList<String>();

        String query = "select student_id from rulangdatabase.courses c\n" +
                "inner join rulangdatabase.teachers t \n" +
                "on c.teacher = t.teacher_id \n" +
                "inner join rulangdatabase.students s\n" +
                "on s.class_id = c.class " +
                "where c.course_id = '" + course_id + "'";
        System.out.println(query);
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                    students.add(rs.getString("student_id"));
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(int i =0; i < students.size(); i++) {
            int error_counter = 0;
            System.out.println(students.get(i));
            query = "select answer from rulangdatabase.task_track t\n" +
                    "inner join rulangdatabase.students s\n" +
                    "on t.student_id = s.student_id\n" +
                    "where t.student_id = '" + students.get(i) +
                    "' and exercise_id in (select max(exercise_id) from rulangdatabase.task_track where student_id = '" + students.get(i)+ "');";
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    if (!rs.getString("answer").equals("correct")) {
                        error_counter++;
                    }
                }


                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Button student = new Button();
            student.setPrefWidth(600);
            student.setAlignment(Pos.CENTER_LEFT);
            query = "select * from rulangdatabase.task_track t\n" +
                    "inner join rulangdatabase.students s\n" +
                    "on t.student_id = s.student_id " +
                    "where t.student_id = '" + students.get(i) + "'";
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    if (error_counter == 0) {
                        student.setText(rs.getString("s.last_name") + " " +
                                rs.getString("s.first_name") + " " + rs.getString("s.middle_name") +
                                "\t\t\t Последнее упражнение было выполнено без ошибок"
                        );
                    } else {
                        student.setText(rs.getString("s.last_name") + " " +
                                rs.getString("s.first_name") + " " + rs.getString("s.middle_name") +
                                "\t\t\t В последнем упражнении допущено " + error_counter + " ошибок");
                    }
                }
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            vBox_that_course.getChildren().add(student);
        }

        anchorPane_courses.getChildren().add(vBox_that_course);

    }

    public void createCourse(){
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

        dialogVbox.getChildren().add(new Text("Создать новый курс"));
        ChoiceBox<String> classChoiceBox = new ChoiceBox<>();
        fillChoiseBox(classChoiceBox);
        ChoiceBox<String> subjectChoiceBox = new ChoiceBox<>();
        subjectChoiceBox.getItems().add("Русский язык");
        dialogVbox.getChildren().addAll(classChoiceBox, subjectChoiceBox);
        Button button_add = new Button("Создать");
        button_add.setOnAction(event1 -> create(dialog, dialogVbox, classChoiceBox.getValue(), subjectChoiceBox.getValue()));
        dialogVbox.getChildren().add(button_add);
        Scene dialogScene = new Scene(dialogVbox, 250, 250);
        dialog.setScene(dialogScene);
        dialog.show();

    }

    private void fillChoiseBox(ChoiceBox<String> choiceBox){
        String query = "select * from rulangdatabase.classes c\n" +
                "inner join rulangdatabase.teachers t \n" +
                "on c.school_id = t.school_id\n" +
                "where login = '" + dbConnector.getActiveUser() +  "' \n" +
                "and c.class_id not in (select class from rulangdatabase.courses c\n" +
                "inner join rulangdatabase.teachers t \n" +
                "on c.teacher = t.teacher_id)";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                choiceBox.getItems().add(rs.getString("class_name"));
                System.out.println(rs.getInt("school_id"));
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void create(Stage dialog, VBox dialogVbox, String class_name, String subject){
        if(class_name != null && subject != null){
        String query = "insert into rulangdatabase.courses(class, subject, teacher)\n" +
                "values((select class_id from rulangdatabase.classes where class_name = '" + class_name + "'),'" + subject + "', (select teacher_id from rulangdatabase.teachers " +
                "where login = '" + dbConnector.getActiveUser() + "'))";;
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            st.close();

            loadVBoxCourses();
            dialog.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }else {
            Label label = new Label("При заполнении остались пустые поля");
            dialogVbox.getChildren().add(label);
    }
    }

    public void exit(ActionEvent event){
        this.dbConnector.setActiveUser("");
        StageLoader.createNewStage(event, "logger.fxml", null,"RuLang Application" );
    }
}