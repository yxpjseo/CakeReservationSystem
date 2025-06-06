package com.cake.service;

import java.util.Scanner;
import com.cake.util.SQLLoader;
import java.sql.*;

public class SignUp {
    public boolean registerUser(Connection conn, Scanner scanner) throws SQLException {
        //사용자 입력
        System.out.print("이름을 입력하세요. : ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("❌ 이름은 필수 항목입니다.");
            return false;
        }

        System.out.print("전화번호를 입력하세요. : ");
        String phone = scanner.nextLine().trim();
        if (!phone.matches("010\\d{8}")) {
            System.out.println("❌ 전화번호는 010으로 시작하는 11자리 숫자여야 합니다.");
            return false;
        }

        System.out.print("이메일을 입력하세요. : ");
        String email = scanner.nextLine().trim();
        if (!email.contains("@")) {
            System.out.println("❌ 유효한 이메일 형식이 아닙니다.");
            return false;
        }

        System.out.print("사용할 비밀번호 4자리를 입력하세요. : ");
        String pw = scanner.nextLine().trim();
        if (!pw.matches("\\d{4}")) {
            System.out.println("❌ 비밀번호는 숫자 4자리여야 합니다.");
            return false;
        }

        //중복 확인
        String checkSQL = SQLLoader.load("check_user_duplicate.sql");
        try (PreparedStatement ps = conn.prepareStatement(checkSQL)) {
            ps.setString(1, phone);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("❌ 이미 등록된 전화번호 또는 이메일입니다.");
                return false;
            }
        }

        //테이블 삽입
        String insertSQL = SQLLoader.load("insert_user.sql");
        try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, pw);
            ps.executeUpdate();
        }

        System.out.println("\n✅ 회원가입이 완료되었습니다.");
        return true;
    }
}
