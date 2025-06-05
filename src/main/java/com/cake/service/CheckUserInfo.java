package com.cake.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.cake.util.SQLLoader;

public class CheckUserInfo {
    public void showUserInfo(Connection conn, int userId) {
        String sql = SQLLoader.load("select_user_by_userId.sql");

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("\n[회원 정보]");
                System.out.println("이름: " + rs.getString("user_name"));
                System.out.println("전화번호: " + rs.getString("phone_num"));
                System.out.println("이메일: " + rs.getString("email"));
                System.out.println();
            } else {
                System.out.println(" 회원 정보를 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

