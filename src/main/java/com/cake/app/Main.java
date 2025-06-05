package com.cake.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String server = "localhost:3306";
        String database = "cake_db";
        String user_name = "root";
        String password = "1234";

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://" + server + "/" + database + "?allowPublicKeyRetrieval=true&useSSL=false",
                user_name,
                password)) {

            runSQL(con, "src/main/resources/sql/schema/create.sql");
            runSQL(con, "src/main/resources/sql/schema/data.sql");

            System.out.println("✅ DB 초기화 완료!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runSQL(Connection conn, String path) throws IOException, SQLException {
        String sql = Files.readString(Path.of(path));
        for (String stmt : sql.split(";")) {
            if (!stmt.trim().isEmpty()) {
                try (Statement s = conn.createStatement()) {
                    s.execute(stmt.trim());
                }
            }
        }
    }
}
