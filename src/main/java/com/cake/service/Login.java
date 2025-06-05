package com.cake.service;

import java.util.Scanner;
import com.cake.util.SQLLoader;
import java.sql.*;

public class Login {
    public int loginUser(Connection conn, Scanner scanner) throws SQLException {
        //사용자 입력
        System.out.print("전화번호를 입력하세요. : ");
        String phone = scanner.nextLine().trim();
        System.out.print("비밀번호 4자리를 입력하세요. : ");
        String pw = scanner.nextLine().trim();

        //정보 일치 확인
        String sql = SQLLoader.load("login_user.sql");

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            ps.setString(2, pw);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("\n✅ 로그인에 성공했습니다.");
                System.out.println(rs.getString("user_name") + "님 어서오세요!\n");
                return rs.getInt("user_id"); // 로그인된 유저 ID 반환
            }
            else {
                System.out.println("로그인 실패. 정보를 다시 확인해주세요.");
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
