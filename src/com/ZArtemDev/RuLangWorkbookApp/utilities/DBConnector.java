package com.ZArtemDev.RuLangWorkbookApp.utilities;

import java.sql.*;

public class DBConnector {

    private static volatile DBConnector instance;
    private String activeUser = "";

    private Connection connection;

    public static DBConnector getInstance() {  //Singleton
        if (instance == null) {
            synchronized (DBConnector.class) {
                if (instance == null) {
                    instance = new DBConnector();
                }
            }
        }
        return instance;
    }

    public DBConnector() {
        final String url = "jdbc:mysql://localhost/rulangdatabase?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
        final String userName = "root";
        final String password = "root";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            this.connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Sql connection opened");

        } catch (Exception e) {
            System.out.println("MySQL connection is broken.");
            System.err.println(e.getMessage());

        }
    }

    public void executeStatement(String query) {
        try {
            System.out.println(query);
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.execute();
            preparedStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Connection getConnection(){
        return this.connection;
    }

    public void setActiveUser(String activeUser) {
        this.activeUser = activeUser;
    }

    public String getActiveUser(){ return  this.activeUser;}

    public void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            this.activeUser = "";
            System.out.println("Sql connection closed");
        }
    }
}

