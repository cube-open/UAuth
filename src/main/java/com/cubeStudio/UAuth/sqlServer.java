package com.cubeStudio.UAuth;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Logger;

public class sqlServer {
    private static int FailCount = 0;
    static int retry_count = 5;
    // MySQL数据库连接信息
    private static String url = "jdbc:mysql://" + ReadYaml.readYamlString("config/config.yml", "Config.sql.url") + ":" + ReadYaml.readYamlValue("config/config.yml", "Config.sql.port") + "/" + ReadYaml.readYamlString("config/config.yml", "Config.sql.db");
    private static String user = ReadYaml.readYamlString("config/config.yml", "Config.sql.user"); // 数据库用户名
    private static String password = ReadYaml.readYamlString("config/config.yml", "Config.sql.passwd");
    ; // 数据库密码
    private static Connection connection;

    public static void con() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {


            if (connection != null) {
                System.out.println("Stopping connection");
                try {
                    connection.close();
                } catch (SQLException e) {
                    Logger.getLogger("this").warning("Error while disconnecting mysql!");
                    LLogger.LogRec("Error while disconnecting mysql!");
                    LLogger.LogRec(Arrays.toString(e.getStackTrace()));
                    LLogger.LogRec("Message: " + e.getMessage());
                    FailCount++;
                    System.out.println();
                    System.out.println("Get more information at kernel.log!");
                    System.out.println("Cause by: " + e.getCause());
                    System.out.println();
                    System.out.println("SQL State: " + e.getSQLState());
                    System.out.println("--------------------------");
                    System.out.println();
                    System.out.println("trace: " + e.getStackTrace());
                    e.printStackTrace();
                    System.out.println("--------------------------");
                    System.out.println();
                    System.out.println("Message: " + e.getMessage());
                    System.out.println();
                    System.out.println("--------------------------");
                    System.out.println("Now server ignore this problem!");
                    throw new RuntimeException(e);

                }
                System.out.println("Connection was closed");
            }

        }));
        if (ReadYaml.readYamlString("config/config.yml", "Config.sql.queryString") != null) {
            url = url + ReadYaml.readYamlString("config/config.yml", "Config.sql.queryString");
        }
        System.out.println("Test sql server......");
        LLogger.LogRec("Test sql server......");
        if (user == null || password == null || ReadYaml.readYamlString("config/config.yml", "Config.sql.url") == null || ReadYaml.readYamlValue("config/config.yml", "Config.sql.port") == null || ReadYaml.readYamlString("config/config.yml", "Config.sql.db") == null) {
            System.err.println("Sql connect info is wrong!Please check it.Stopping server......");
            LLogger.LogRec("Sql connect info is wrong or null!Stopping server...");
            System.exit(-1);
        }
        System.out.println("Loading drivers......");
        LLogger.LogRec("Loading drivers......");
        Driver driver = null;
        try {
            driver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(driver);
            System.out.println("Done!");
            LLogger.LogRec("Done!");
        } catch (SQLException e) {
            Logger.getLogger("this").warning("Error!Failed to load drivers!");
            LLogger.LogRec("Error!Failed to load drivers!");
            LLogger.LogRec(Arrays.toString(e.getStackTrace()));
            System.out.println("Get more information at kernel.log!");
            System.out.println("Cause by: " + e.getCause() + " " + e.getMessage());
            System.exit(-1);
            throw new RuntimeException(e);

        }


        while (true) {
            try {
                System.out.println("Trying to connect mysql server at " + url + " ......");
                System.out.println("User: " + user);
                //连接mariadb/mysql
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Mysql connector loaded at: " + connection);
                LLogger.LogRec("Mysql connector loaded at: " + connection);
                System.out.println("test connected successful");
                LLogger.LogRec("test connected successful");
                System.out.println("Closing connect ......");
                LLogger.LogRec("Closing connect ......");
                connection.close();
                LLogger.LogRec("Done");
                System.out.println("Done");
                break;
            } catch (SQLException e) {
                Logger.getLogger("this").warning("Error while connecting mysql!");
                LLogger.LogRec("Error while connecting mysql!");
                retry_count = 5;
                LLogger.LogRec(Arrays.toString(e.getStackTrace()));
                LLogger.LogRec("Message: " + e.getMessage());
                FailCount++;
                System.out.println();
                System.out.println("Get more information at kernel.log!");
                System.out.println("Cause by: " + e.getCause());
                System.out.println();
                System.out.println("SQL State: " + e.getSQLState());
                System.out.println("--------------------------");
                System.out.println();
                System.out.println("trace: " + e.getStackTrace());
                e.printStackTrace();
                System.out.println("--------------------------");
                System.out.println();
                System.out.println("Message: " + e.getMessage());
                System.out.println();
                System.out.println("--------------------------");
                if (FailCount > 10) {
                    System.out.println("Test failed!Stopping server......");
                    System.exit(-1);
                }
                while (retry_count >= 0) {

                    System.out.println("Server will try again after " + retry_count + " seconds.");
                    if (ReadYaml.readYamlString("config/config.yml", "Config.sql.queryString") != null) {
                        url = url + ReadYaml.readYamlString("config/config.yml", "Config.sql.queryString");
                    }
                    retry_count--;
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);

                    }
                }

                System.out.println("Retrying" + FailCount + "/10" + "......");
                try {
                    Thread.currentThread().sleep(1500);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                password = ReadYaml.readYamlString("config/config.yml", "Config.sql.passwd");
                user = ReadYaml.readYamlString("config/config.yml", "Config.sql.user");
                url = "jdbc:mysql://" + ReadYaml.readYamlString("config/config.yml", "Config.sql.url") + ":" + ReadYaml.readYamlValue("config/config.yml", "Config.sql.port") + "/" + ReadYaml.readYamlString("config/config.yml", "Config.sql.db");
                LLogger.LogRec("Retrying to connect mysql......");
            }
        }
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
