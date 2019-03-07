package com.ZArtemDev.RuLangWorkbookApp;

import java.sql.*;

public class DBConnector {
    private static String activeUser;
    private static short userType;

    private String dbName;

    private static Connection connection = null;
    private final String url = "jdbc:mysql://localhost/users?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    private final String userName = "root";
    private final String password = "root";

    public DBConnector(){
    }

    public void executeStatement(String query){
        try {
            System.out.println(query);
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.execute();
            preparedStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getDatabases(){
        connectDataBase();

        String tmp = "";
        try {
            String query = "SHOW DATABASES";

            System.out.println(query);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                tmp += rs.getString("database") + '\n';
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(tmp);

        disconnectDataBase();

        return tmp;

    }

    public boolean checkUser(String username, String password) {
        try {
            String query = "SELECT * FROM users WHERE username = '" + username + "' and password = '" + password + "'";

            System.out.println(query);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                activeUser= rs.getString("username");
                userType = rs.getShort("type");
                return true;
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void connectDataBase(){
        try {
            connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Sql database connected");
    }

    public void disconnectDataBase(){
        if(connection != null)
        {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Sql database disconnected");
        }
    }

    Connection getConnection(){
        return connection;
    }
}
