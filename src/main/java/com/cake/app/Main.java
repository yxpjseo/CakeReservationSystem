package com.cake.app;

import com.cake.util.DButil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    
    // DB 초기화
    public static void main(String[] args) {
        try (Connection con = DButil.getConnection()) {
            runSQL(con, "sql/schema/dropschema.sql");
            runSQL(con, "sql/schema/createschema.sql");
            runSQL(con, "sql/schema/initdata.sql");

            System.out.println("✅ DB 초기화 완료!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 지정된 SQL 파일 읽고 실행
    public static void runSQL(Connection conn, String resourcePath) throws IOException, SQLException {
        InputStream in = Main.class.getClassLoader().getResourceAsStream(resourcePath);
        if (in == null) {
            throw new IOException("❌ Resource not found: " + resourcePath);
        }

        String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        for (String stmt : sql.split(";")) {
            if (!stmt.trim().isEmpty()) {
                try (Statement s = conn.createStatement()) {
                    s.execute(stmt.trim());
                }
            }
        }
    }
}
