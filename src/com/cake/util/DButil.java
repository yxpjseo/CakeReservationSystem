package com.cake.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DButil {
    private static final String URL = "jdbc:mysql://localhost:3306/cake_ordering";
    private static final String USER = "root";
    private static final String PASSWORD = "happy1935";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
