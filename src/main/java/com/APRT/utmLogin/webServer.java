package com.APRT.utmLogin;
import java.sql.*;

public class webServer {

    // MySQL数据库连接信息
    //private static final String url = "jdbc:mysql://localhost:3306/testdb"; // 数据库URL
    private static final String url = "jdbc:mysql://"+ReadYaml.readYamlString("config/config.yml","Config.sql.url") +ReadYaml.readYamlValue("config/config.yml","Config.sql.port") + "/" + ReadYaml.readYamlString("config/config.yml","Config.sql.db");
    private static final String user = "root"; // 数据库用户名
    private static final String password = "password"; // 数据库密码

    public static void con(){
        //sql连接，等待开发

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