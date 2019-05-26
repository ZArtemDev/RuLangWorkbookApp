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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBConnector dbConnector = DBConnector.getInstance();
        Connection connection = dbConnector.getConnection();

        String query = "select distinct(federal_district) from rulangdatabase.locations order by federal_district asc";

        ComboBox<String> districtComboBox = new ComboBox<String>(getList(connection, query));
        districtComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (districtComboBox.getValue() != null) {
                    if (flow_pane.getChildren().size() > 1) {
                        flow_pane.getChildren().remove(flow_pane.getChildren().size() - 1);
                    }
                    String query = "select distinct(region) from rulangdatabase.locations where federal_district = '"
                            + districtComboBox.getValue() + "' order by region asc";
                    ComboBox<String> regionComboBox = new ComboBox<String>(getList(connection, query));
                    regionComboBox.valueProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            if (regionComboBox.getValue() != null) {
                                if (flow_pane.getChildren().size() > 2) {
                                    flow_pane.getChildren().remove(flow_pane.getChildren().size() - 1);
                                }
                                String query = "select distinct(location) from rulangdatabase.locations where federal_district = '"
                                        + districtComboBox.getValue() + "' and region = '" + regionComboBox.getValue()
                                        + "' order by location asc";
                                ComboBox<String> locationComboBox = new ComboBox<String>(getList(connection, query));
                                locationComboBox.valueProperty().addListener(new ChangeListener<String>() {
                                    @Override
                                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                        TextField schoolTextField = new TextField();
                                        Button commitBtn = new Button("Добавить школу");
                                        commitBtn.setOnAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent event) {
                                                String query = "insert into rulangdatabase.schools(school_name, location_id) "
                                                        + "values('" + schoolTextField.getText()
                                                        + "', (select id from rulangdatabase.locations where location = '"
                                                        + locationComboBox.getValue() + "'));";
                                                try {
                                                    Statement st = connection.createStatement();
                                                    st.executeUpdate(query);
                                                    st.close();
                                                } catch (SQLException e) {
                                                    flow_pane.getChildren().add(new Label("Школа с таким именем уже существует"));
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        flow_pane.getChildren().addAll(schoolTextField, commitBtn);
                                    }
                                });

                                flow_pane.getChildren().add(locationComboBox);
                            }
                        }
                    });

                    flow_pane.getChildren().add(regionComboBox);
                }
            }
        });

        flow_pane.getChildren().add(districtComboBox);
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
