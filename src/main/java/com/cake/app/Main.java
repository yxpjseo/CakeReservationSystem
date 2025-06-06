package com.cake.app;

import com.cake.util.DButil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        try (Connection con = DButil.getConnection()) {
            runSQL(con, "src/main/resources/sql/schema/dropschema.sql");
            runSQL(con, "src/main/resources/sql/schema/createschema.sql");
            runSQL(con, "src/main/resources/sql/schema/initdata.sql");

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
