package com.ZArtemDev.RuLangWorkbookApp.utilities;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Scanner;

public class TMPCitiesAdd implements Initializable {
    @FXML
    VBox root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBConnector dbConnector = DBConnector.getInstance();
        Connection connection = dbConnector.getConnection();

        String[] tmp = new String[32];


        FileInputStream fis = null;

        try {
            fis = new FileInputStream("C:/dev/Java/RuLangWorkbookApp/src/com/ZArtemDev/RuLangWorkbookApp/resources/schools_vn.txt");
            Scanner dis = new Scanner(fis);
            int i = 0;
            while (dis.hasNextLine()) {
                tmp[i] = dis.nextLine();
                i++;
            }
            System.out.println(i);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 32; i++)
            System.out.println(tmp[i]);
        String a = "МАОУ «СОШ № 34 с углубленным изучением обществознания и экономики»";
        System.out.println(a.length());

        System.out.println("INSERT INTO rulangdatabase.schools (school_name, location_id) VALUES('" + tmp[0] + "', 146)");
        try {
                Statement st = connection.createStatement();
                for(int i = 0; i < 32; i++) {
                    st.executeUpdate("INSERT INTO rulangdatabase.schools (school_name, location_id) VALUES('" + tmp[i] + "', 146)");
                }
                st.close();

        }catch (SQLException ex) {
            System.out.println("ERROR connection");
            ex.printStackTrace();
        }

    }
}
