package com.ZArtemDev.RuLangWorkbookApp.student;

import com.ZArtemDev.RuLangWorkbookApp.LoggerController;
import com.ZArtemDev.RuLangWorkbookApp.StageLoader;
import com.ZArtemDev.RuLangWorkbookApp.student.task.InsertLetterTask;
import com.ZArtemDev.RuLangWorkbookApp.student.task.Task;
import com.ZArtemDev.RuLangWorkbookApp.student.task.TypeLetterTask;
import com.ZArtemDev.RuLangWorkbookApp.utilities.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class StudentController implements Initializable {

    private final static String insertLetterTask= "Верно переместите буквы в места пропуска в словах.";
    private final static String typeLetterTask= "Вставьте пропущеные в словах буквы.";


    private Task task;
    private LinkedList<Node> ll;
    private Stack<Integer> rows;
    private short counter;
    private int type;
    private int user_id;
    private int exercise_counter;
    private int task_id;

    @FXML
    private VBox vBox_task;

    private DBConnector dbConnector = null;
    private Connection connection = null;

    @FXML
    VBox vBox_progress_n_labels;

    @FXML
    Label label_username, label_task_number, label_task_goal;

    @FXML
    Button button_exit, button_start, button_next;

    @FXML
    ProgressBar progressBar_progress;

    @FXML
    BorderPane root, borderPane_task;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        this.dbConnector = DBConnector.getInstance();
        this.connection = dbConnector.getConnection();
        initUser(dbConnector.getActiveUser());
        borderPane_task.getChildren().remove(button_next);
        borderPane_task.getChildren().remove(vBox_progress_n_labels);
        vBox_task = new VBox();
        vBox_task.setId("vBox_task");

        button_start.setOnAction(event -> startExam());

        ll = new LinkedList<>();
        rows = new Stack<>();
    }

    private void startExam(){
        String query = "select count(1) from (select distinct(exercise_id) " +
                "from rulangdatabase.task_track where student_id = '" + user_id + "') as exercises";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                exercise_counter = rs.getInt("count(1)") + 1;
                System.out.println(exercise_counter);
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        progressBar_progress.setProgress(0.0);
        counter = 1;
        borderPane_task.setTop(vBox_progress_n_labels);
        borderPane_task.setBottom(button_next);
        borderPane_task.getChildren().remove(button_start);
        borderPane_task.setCenter(vBox_task);
        button_next.setOnAction(event -> nextTask());
        firsTask();
    }

    private void firsTask(){
        short tableSize = 0;
        String query = "select count(1) from rulangdatabase.tasks";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                tableSize = rs.getShort("count(1)");
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(int i = 1; i <= tableSize; i++){
            rows.add(i);
        }
        Collections.shuffle(rows);

        vBox_task.getChildren().clear();
        progressBar_progress.setProgress(progressBar_progress.getProgress() + 0.1);
        label_task_number.setText("Задание " + counter++ + ".");

        getTask();

        if(type == 0) label_task_goal.setText(insertLetterTask);
        else if(type == 1) label_task_goal.setText(typeLetterTask);

        ll = task.getContentList();

        vBox_task.getChildren().addAll(task.coverIntoContainers(ll));
    }

    private void nextTask(){

        if(progressBar_progress.getProgress() < 0.9){
            progressBar_progress.setProgress(progressBar_progress.getProgress() + 0.1);
            label_task_number.setText("Задание " + counter++ + ".");

            vBox_task.getChildren().clear();

            String answer = task.getAnswer(ll);

            String query = "insert into rulangdatabase.task_track(student_id, task_id, exercise_id, answer, date_n_time)\n" +
                    "values('" + user_id + "','" + task_id + "','" + exercise_counter + "','" + answer + "', now());";
            try {
                Statement st = connection.createStatement();
                st.executeUpdate(query);
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            getTask();
            if(type == 0) label_task_goal.setText(insertLetterTask);
            else if(type == 1) label_task_goal.setText(typeLetterTask);

            ll = task.getContentList();

            vBox_task.getChildren().addAll(task.coverIntoContainers(ll));
        }else {
            borderPane_task.getChildren().clear();
            borderPane_task.setCenter(button_start);
        }

    }

    private void getTask(){


        int row;
        Random random = new Random();
        type = random.nextInt(2);
        row = rows.pop();

        String query = "select * from rulangdatabase.tasks where task_id = '" + row + "'";
        String content = null;
        String answer = null;

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            rs.next();
            task_id = rs.getInt("task_id");
            content = rs.getString("task_text");
            answer = rs.getString("task_answer");

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(content != null && answer != null){
            if(type == 0){
                System.out.println("InsertLetterTask");
                task = new InsertLetterTask(content, answer);
            }else  if(type == 1){
                System.out.println("TypeLetterTask");
                task = new TypeLetterTask(content, answer);
            }
        }
    }

    public void initUser(String user){
        String query = "select * from rulangdatabase.students where login = '" + user + "'";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                label_username.setText(rs.getString("first_name") + " " + rs.getString("last_name"));
                user_id = rs.getInt("student_id");
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void exit(ActionEvent event){
        this.dbConnector.setActiveUser("");
        StageLoader.createNewStage(event, "logger.fxml", null,"RuLang Application" );
    }
}
