package com.cake.service;

import java.util.Scanner;
import java.sql.*;
import com.cake.util.SQLLoader;

public class CancelAccount {
    
    // 회원 탈퇴
    public void deleteUserAccount(Connection conn, Scanner scanner, int userId) throws SQLException {
        System.out.println("\n*회원 탈퇴 시 모든 예약 정보가 삭제되며 복구되지 않습니다.*");
        System.out.print("회원 탈퇴를 진행하시겠습니까? (Y/N) >> ");
        String confirm = scanner.nextLine().trim().toUpperCase();
        if (!confirm.equals("Y")) return;

        //비밀번호 재확인
        System.out.print("비밀번호 4자리를 입력하세요 : ");
        String inputPw = scanner.nextLine().trim();

        String pwCheckSQL = SQLLoader.load("select_user_pw.sql");
        try (PreparedStatement ps = conn.prepareStatement(pwCheckSQL)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String realPw = rs.getString("pw");
                if (!realPw.equals(inputPw)) {
                    System.out.println("❌ 비밀번호가 일치하지 않습니다.");
                    return;
                }
            } else {
                System.out.println("❌ 사용자 정보가 존재하지 않습니다.");
                return;
            }
        }

        //회원 정보 삭제
        conn.setAutoCommit(false);
        try (
                PreparedStatement deletePickups = conn.prepareStatement(
                        SQLLoader.load("delete_pickups_by_user.sql"));
                PreparedStatement deleteItems = conn.prepareStatement(
                        SQLLoader.load("delete_items_by_user.sql"));
                PreparedStatement deleteOrders = conn.prepareStatement(
                        SQLLoader.load("delete_order_by_user.sql"));
                PreparedStatement deleteUser = conn.prepareStatement(
                        SQLLoader.load("delete_user.sql"));
        ) {
            // 예약 정보 순차 삭제
            deletePickups.setInt(1, userId);
            deletePickups.executeUpdate();

            deleteItems.setInt(1, userId);
            deleteItems.executeUpdate();

            deleteOrders.setInt(1, userId);
            deleteOrders.executeUpdate();

            // 마지막으로 사용자 삭제
            deleteUser.setInt(1, userId);
            deleteUser.executeUpdate();

            conn.commit();
            System.out.println("\n✅ 회원 탈퇴가 완료되었습니다.");
            System.out.println("지금까지 이용해주셔서 감사합니다.");
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("❌ 탈퇴 처리 중 오류가 발생했습니다. 다시 시도해주세요.");
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

}
