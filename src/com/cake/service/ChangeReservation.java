package com.cake.service;

import java.util.*;
import java.sql.*;
import java.time.*;

import com.cake.util.SQLLoader;

public class ChangeReservation {

    public void showUserReservations(Connection conn, int userId, Scanner scanner) throws SQLException {
        String sql = SQLLoader.load("select_user_reservations.sql");

        Map<Integer, List<String>> cakeMap = new LinkedHashMap<>();
        Map<Integer, Integer> candlesMap = new HashMap<>();
        Map<Integer, String> pickupMap = new HashMap<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String cakeInfo = rs.getString("cake_name") + " " + rs.getInt("count") + "개";

                cakeMap.computeIfAbsent(orderId, k -> new ArrayList<>()).add(cakeInfo);
                candlesMap.putIfAbsent(orderId, rs.getInt("candles"));
                pickupMap.putIfAbsent(orderId, rs.getDate("pickup_date") + " " + rs.getInt("pickup_time") + ":00");
            }
        }

        System.out.println("예약 현황입니다.\n");
        for (int orderId : cakeMap.keySet()) {
            System.out.println("[주문번호: " + orderId + "]");
            System.out.println("- 케이크: " + String.join(", ", cakeMap.get(orderId)));
            System.out.println("- 초 수량: " + candlesMap.get(orderId) + "개");
            System.out.println("- 피커브 일시: " + pickupMap.get(orderId));
            System.out.println();
        }

        System.out.println("0. 홈 메뉴로 돌아가기");
        System.out.println("1. 예약 변경");
        System.out.println("2. 예약 취소");
        System.out.print("선택 >> ");
        int menu = Integer.parseInt(scanner.nextLine());

        if (menu == 1) updateReservation(conn, scanner);
        else if (menu == 2) cancelReservation(conn, scanner);
    }

    public void updateReservation(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("\n주문번호를 입력하세요 >> ");
        int orderId = Integer.parseInt(scanner.nextLine());
        showReservationDetail(conn, orderId);

        String query = SQLLoader.load("select_reservation_detail.sql");
        LocalDate pickupDate = null;
        int pickupTime = 0;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pickupDate = rs.getDate("pickup_date").toLocalDate();
                pickupTime = rs.getInt("pickup_time");
            }
        }

        if (pickupDate.equals(LocalDate.now())) {
            System.out.println("❌ 픽업 당일에는 예약 변경이 불가능합니다.");
            return;
        }

        System.out.println("\n예약 변경은 다음 항목만 가능합니다:");
        System.out.println("- 픽업 시간 변경\n- 초 수량 변경");
        System.out.println("\n📌 픽업 날짜나 케이크 종류 변경은 예약 취소 후 새로 예약해주세요.\n");
        System.out.println("1. 픽업 시간 변경\n2. 초 수량 변경\n0. 홈 메뉴로 돌아가기");
        System.out.print("선택 >> ");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == 1) {
            System.out.printf("\n현재 예약된 날짜: %s\n", pickupDate);
            System.out.printf("예약된 시간: %02d시\n", pickupTime);

            int newTime = CakeReservation.promptPickupTime(scanner, pickupDate.toString(), conn);
            try (PreparedStatement ps = conn.prepareStatement(SQLLoader.load("update_pickup_time.sql"))) {
                ps.setInt(1, newTime);
                ps.setInt(2, orderId);
                ps.executeUpdate();
                System.out.println("✅ 픽업 시간이 " + newTime + "시로 변경되었습니다!");
            }
        } else if (choice == 2) {
            updateCandles(conn, scanner, orderId);
        }
    }

    public void showReservationDetail(Connection conn, int orderId) throws SQLException {
        String sql = SQLLoader.load("select_reservation_detail.sql");

        List<String> cakeItems = new ArrayList<>();
        int candles = -1;
        String pickupDate = "";
        String pickupTime = "";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                String cakeName = rs.getString("cake_name");
                int count = rs.getInt("count");
                cakeItems.add(cakeName + " " + count + "개");

                if (candles == -1) {
                    candles = rs.getInt("candles");
                    pickupDate = rs.getDate("pickup_date").toString();
                    pickupTime = rs.getInt("pickup_time") + ":00";
                }
            }

            if (!found) {
                System.out.println("❌ 해당 주문번호에 대한 예약 정보가 없습니다.");
                return;
            }

            System.out.printf("[주문번호: %d]\n", orderId);
            System.out.println("- 케이크: " + String.join(", ", cakeItems));
            System.out.println("- 초 수량: " + candles + "개");
            System.out.println("- 픽업 일시: " + pickupDate + " " + pickupTime);
        }
    }

    public void updateCandles(Connection conn, Scanner scanner, int orderId) throws SQLException {
        String sql = SQLLoader.load("select_reservation_detail.sql");
        int currentCandles = -1;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                currentCandles = rs.getInt("candles");
            }
        }

        System.out.println("현재 초 개수: " + currentCandles + "개");
        System.out.print("변경할 초 개수를 입력하세요. (0 이상 숫자) >> ");
        int newCandles = Integer.parseInt(scanner.nextLine());

        if (newCandles < 0 || newCandles > 100) {
            System.out.println("❌ 초 수량은 0~100개 사이만 가능합니다.");
            return;
        }

        String updateSQL = SQLLoader.load("update_candles.sql");
        try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {
            ps.setInt(1, newCandles);
            ps.setInt(2, orderId);
            ps.executeUpdate();
            System.out.println("✅ 초 개수가 " + newCandles + "개로 변경되었습니다.");
        }
    }

    public void cancelReservation(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("\n주문번호를 입력하세요 >> ");
        int orderId = Integer.parseInt(scanner.nextLine());

        String detailSql = SQLLoader.load("select_reservation_detail.sql");

        try (PreparedStatement ps = conn.prepareStatement(detailSql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            List<String> items = new ArrayList<>();
            String pickupDate = "", pickupTime = "";
            while (rs.next()) {
                items.add(rs.getString("cake_name") + " " + rs.getInt("count") + "개");
                pickupDate = rs.getDate("pickup_date").toString();
                pickupTime = rs.getInt("pickup_time") + ":00";
            }

            if (items.isEmpty()) {
                System.out.println("❌ 해당 주문번호에 대한 예약이 없습니다.");
                return;
            }

            System.out.printf("\n[주문번호: %d]\n- 케이크: %s\n- 픽업 일시: %s %s\n",
                    orderId, String.join(", ", items), pickupDate, pickupTime);
        }

        System.out.print("해당 예약을 취소하시겠습니까? [Y/N] >> ");
        String confirm = scanner.nextLine().trim().toUpperCase();
        if (!confirm.equals("Y")) return;

        conn.setAutoCommit(false);
        try (
                PreparedStatement deletePick = conn.prepareStatement(SQLLoader.load("delete_pickups_by_orderId.sql"));
                PreparedStatement deleteItems = conn.prepareStatement(SQLLoader.load("delete_order_items_by_orderId.sql"));
                PreparedStatement deleteOrder = conn.prepareStatement(SQLLoader.load("delete_order_by_orderId.sql"))
        ) {
            deletePick.setInt(1, orderId);
            deletePick.executeUpdate();

            deleteItems.setInt(1, orderId);
            deleteItems.executeUpdate();

            deleteOrder.setInt(1, orderId);
            deleteOrder.executeUpdate();

            conn.commit();
            System.out.println("✅ 예약이 취소되었습니다. 감사합니다. 다음에 또 방문해주세요 🍰");
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("❌ 예약 취소 중 오류가 발생했습니다.");
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
