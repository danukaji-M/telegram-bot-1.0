package com.ruufilms.config;

import java.sql.*;

public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/ruu_film";
    private static final String USER = "test";
    private static final String PASSWORD = "Danu2003@";
    private static Connection connection = null;

    public static Connection getConnection () throws SQLException {
        if(connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(URL,USER,PASSWORD);
        }
        return connection;
    }

    public static ResultSet executeQuery(String query) throws SQLException{
        Connection con = getConnection();
        Statement statement = con.createStatement();
        return statement.executeQuery(query);
    }

    public static int executeUpdate(String query) throws SQLException {
        Connection conn = getConnection();
        Statement statement = conn.createStatement();
        return statement.executeUpdate(query);
    }

    public static void closeConnection() throws SQLException{
        if(connection != null && !connection.isClosed()){
            connection.close();
        }
    }
}
