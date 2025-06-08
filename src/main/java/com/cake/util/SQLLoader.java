package com.cake.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class SQLLoader {
    private static final String BASE_PATH = "sql/operation/";

    // SQL 쿼리 파일 읽어오기
    public static String load(String fileName) {
        String fullPath = BASE_PATH + fileName;
        try (InputStream in = SQLLoader.class.getClassLoader().getResourceAsStream(fullPath)) {
            if (in == null) throw new FileNotFoundException("SQL 파일을 찾을 수 없습니다: " + fullPath);
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("SQL 파일 로딩 실패: " + fullPath, e);
        }
    }
}
