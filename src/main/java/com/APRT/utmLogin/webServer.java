package com.APRT.utmLogin;
import java.sql.*;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.logging.Logger;

public class webServer {

    // MySQL数据库连接信息
    private static final String url = "jdbc:mysql://"+ReadYaml.readYamlString("config/config.yml","Config.sql.url") +":"+ReadYaml.readYamlValue("config/config.yml","Config.sql.port") + "/" + ReadYaml.readYamlString("config/config.yml","Config.sql.db");
    private static final String user = ReadYaml.readYamlString("config/config.yml","Config.sql.user"); // 数据库用户名
    private static final String password = ReadYaml.readYamlString("config/config.yml","Config.sql.passwd");; // 数据库密码

    public static void con() {
        //sql连接，等待开发
        System.out.println("Test sql server......");
        LLogger.LogRec("Test sql server......");
        if (user == null || password == null || ReadYaml.readYamlString("config/config.yml", "Config.sql.url") == null || ReadYaml.readYamlValue("config/config.yml", "Config.sql.port") == null || ReadYaml.readYamlString("config/config.yml", "Config.sql.db") == null) {
            System.err.println("Sql connect info is wrong!Please check it.Stopping server......");
            LLogger.LogRec("Sql connect info is wrong or null!Stopping server...");
            System.exit(-1);
        }
        System.out.println("Loading drivers......");
        LLogger.LogRec("Loading drivers......");
        try { // 加载数据库驱动类
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Done!");
            LLogger.LogRec("Done!");

        } catch (ClassNotFoundException e) {
            Logger.getLogger("this").warning("Error!Failed to load drivers!");
            LLogger.LogRec("Error!Failed to load drivers!");
            LLogger.LogRec(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            System.exit(-1);
        }

        try {
            System.out.println("Trying to connect mysql server......");
            //连接mariadb/mysql
            DriverManager.getConnection(url, user, password);
            System.out.println("Connected successful");
            LLogger.LogRec("Connected successful");
        } catch (SQLException e) {
            Logger.getLogger("this").warning("Error while connecting mysql!");
            LLogger.LogRec("Error while connecting mysql!");
            LLogger.LogRec(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            System.exit(-1);


        }
    }
    /*
    // 建立数据库连接的私有辅助方法
    private static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Failed to load MySQL JDBC driver.");
        }
        return DriverManager.getConnection(url, user, password);
    }

    // 获取用户信息
    public static String getUser(String username, String password) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("User found: " + username + ", " + password);
                return username+ " " + password;

            } else {
                System.out.println("User not found.");
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return username;
    }

    // 创建用户
    public static void createUser(String username, String password) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            System.out.println("User created: " + username + ", " + password);

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // 检查用户是否存在
    public static boolean checkUser(String username) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("User exists: " + username);
                return true;
            } else {
                System.out.println("User does not exist.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return false;
    }

     */
}