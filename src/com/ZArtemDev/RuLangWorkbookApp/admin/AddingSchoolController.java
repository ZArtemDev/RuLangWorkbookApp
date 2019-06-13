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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AddingSchoolController implements Initializable {
    @FXML
    AnchorPane root;

    @FXML
    FlowPane flow_pane;

    @FXML
    ComboBox<String> comboBox_region, comboBox_district, comboBox_location, comboBox_school;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBConnector dbConnector = DBConnector.getInstance();
        Connection connection = dbConnector.getConnection();

        flow_pane.setHgap(10);

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
                            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                flow_pane.getChildren().clear();
                                TextField schoolTextField = new TextField();
                                flow_pane.getChildren().add(schoolTextField);
                                Button button = new Button("Добавить школу");
                                flow_pane.getChildren().add(button);

                                Label label_info = new Label();
                                flow_pane.getChildren().add(label_info);
                                button.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        if (!schoolTextField.getText().isEmpty()) {
                                            String query = "insert into rulangdatabase.schools(school_name, location_id) "
                                                    + "values('" + schoolTextField.getText()
                                                    + "', (select id from rulangdatabase.locations where location = '"
                                                    + comboBox_location.getValue() + "'));";
                                            try {
                                                Statement st = connection.createStatement();
                                                st.executeUpdate(query);
                                                st.close();
                                                label_info.setText("Школа добавлена");
                                            } catch (SQLException e) {
                                                label_info.setText("Школа с таким именем уже существует");
                                                e.printStackTrace();
                                            }
                                        }else {
                                            label_info.setText("Введите название школы");
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
