package com.cake.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DButil {
    private static final String URL = "jdbc:mysql://localhost:3306/cake_db";
    private static final String USER = "root";
    private static String PASSWORD = null;

    static {
        Scanner scanner = new Scanner(System.in);
        System.out.print("🔐 MySQL 비밀번호를 입력하세요: ");
        PASSWORD = scanner.nextLine();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
