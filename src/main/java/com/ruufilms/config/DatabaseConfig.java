package com.ruufilms.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

public class DatabaseConfig {
    static AppConfig.Config config = new AppConfig.Config(AppConfig.INSTANCE.properties);
    public static String URL = config.getDbHost();
    public static String USER = config.getDbUser();
    public static String PASSWORD = config.getDbPassword();
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

    public static int executePreparedUpdate(String sql, Object... params) throws SQLException {
        Connection conn = getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement.executeUpdate();
        }
    }

    public static ResultSet executePreparedQuery(String sql, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        // Set parameters in the query
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement.executeQuery();  // Executes the SELECT query and returns the ResultSet
    }


    public static void closeConnection() throws SQLException{
        if(connection != null && !connection.isClosed()){
            connection.close();
        }
    }
}
