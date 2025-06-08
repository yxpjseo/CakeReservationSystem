package com.cake.service;

import java.sql.*;
import java.util.*;
import com.cake.util.*;

public class CakeReservation {

    public void execute(Scanner sc, int userId) {
        Map<String, Integer> cart = new LinkedHashMap<>();

        // 케이크 메뉴 출력
        try (Connection conn = DButil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQLLoader.load("select_all_cakes.sql"));
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n[ 케이크 메뉴 ]");
            System.out.printf("%-15s %-6s %-10s %-5s\n", "이름", "사이즈", "가격", "재고");
            while (rs.next()) {
                System.out.printf("%-15s %d호 %10d원 %5d개\n",
                        rs.getString("cake_name"), rs.getInt("size"),
                        rs.getInt("price"), rs.getInt("stock"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // 사용자 입력
        while (true) {
            System.out.print("\n케이크 이름을 입력해주세요: ");
            String cake = sc.nextLine();
            System.out.print("수량을 입력해주세요 (1 이상): ");
            int qty = Integer.parseInt(sc.nextLine());

            if (!checkStock(cake, qty)) {
                System.out.println("// 재고 부족으로 구매가 불가능합니다.");
            } else {
                cart.put(cake, cart.getOrDefault(cake, 0) + qty);
            }

            System.out.print("케이크를 추가로 구매하시겠습니까? (Y/N): ");
            if (!sc.nextLine().trim().equalsIgnoreCase("Y")) break;
        }

        if (cart.isEmpty()) {
            System.out.println("선택된 케이크가 없습니다.");
            return;
        }


        System.out.print("\n초는 총 몇 개 필요하신가요?: ");
        int candles = Integer.parseInt(sc.nextLine());

        System.out.print("\n픽업 날짜를 입력해주세요 (예: 2025-06-15): ");
        String date = sc.nextLine();

        //  주문 등록 + 픽업시간 입력
        try (Connection conn = DButil.getConnection()) {

            int time = promptPickupTime(sc, date, conn); // 픽업시간 통합 메서드 호출
            conn.setAutoCommit(false);

            int totalPrice = 0;
            for (String cake : cart.keySet()) {
                totalPrice += getPrice(cake) * cart.get(cake);
            }

            // orders 테이블 삽입
            PreparedStatement orderStmt = conn.prepareStatement(SQLLoader.load("insert_order.sql"), Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, userId);
            orderStmt.setInt(2, totalPrice);
            orderStmt.setDate(3, java.sql.Date.valueOf(date));
            orderStmt.setInt(4, candles);
            orderStmt.executeUpdate();

            ResultSet keyRs = orderStmt.getGeneratedKeys();
            keyRs.next();
            int orderId = keyRs.getInt(1);

            // order_items 삽입
            PreparedStatement itemStmt = conn.prepareStatement(SQLLoader.load("insert_order_item.sql"));
            for (Map.Entry<String, Integer> entry : cart.entrySet()) {
                itemStmt.setInt(1, orderId);
                itemStmt.setString(2, entry.getKey());
                itemStmt.setInt(3, entry.getValue());
                itemStmt.addBatch();
            }
            itemStmt.executeBatch();

            // 재고 차감
            PreparedStatement updateStockStmt = conn.prepareStatement(
                    SQLLoader.load("update_cake_stock.sql")
            );
            for (Map.Entry<String, Integer> entry : cart.entrySet()) {
                updateStockStmt.setInt(1, entry.getValue());   // 수량
                updateStockStmt.setString(2, entry.getKey());  // 케이크명
                updateStockStmt.addBatch();
            }
            updateStockStmt.executeBatch();


            // pick_ups 삽입
            PreparedStatement pickupStmt = conn.prepareStatement(SQLLoader.load("insert_pickup.sql"));
            pickupStmt.setDate(1, java.sql.Date.valueOf(date));
            pickupStmt.setInt(2, time);
            pickupStmt.setInt(3, orderId);
            pickupStmt.executeUpdate();

            conn.commit();

            // 주문 확인 출력 (DB에서 받아온 정보 기반)
            String receiptSql = SQLLoader.load("select_order_receipt.sql");
            try (PreparedStatement receiptStmt = conn.prepareStatement(receiptSql)) {
                receiptStmt.setInt(1, orderId);
                ResultSet rs = receiptStmt.executeQuery();

                System.out.println("\n[ 주문번호: " + orderId + " ]");
                System.out.println("-------------------------------------");
                System.out.printf("%-15s %4s %9s %9s\n", "케이크명", "수량", "단가", "금액");
                System.out.println("-------------------------------------");

                int totalFromReceipt = 0;
                while (rs.next()) {
                    String cakeName = rs.getString("cake_name");
                    int count = rs.getInt("count");
                    int price = rs.getInt("price");
                    int itemTotal = count * price;
                    totalFromReceipt += itemTotal;

                    System.out.printf("%-15s %3d개 %,9d원 %,9d원\n",
                            cakeName, count, price, itemTotal);
                }

                System.out.println("-------------------------------------");
                System.out.println("- 초 수량: " + candles + "개");
                System.out.printf("- 픽업 일시: %s %02d:00\n", date, time);
                System.out.printf("총 결제 금액: %,d원\n", totalFromReceipt);
                System.out.println("\n예약이 완료되었습니다! 감사합니다 :)");

            } catch (SQLException e) {
                e.printStackTrace();
            }


        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = DButil.getConnection()) {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }

    // 가격 조회
    private int getPrice(String cake) {
        String sql = SQLLoader.load("get_cake_price.sql");
        try (Connection conn = DButil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cake);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt("price") : 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    //  재고 확인
    private boolean checkStock(String cake, int qty) {
        String sql = SQLLoader.load("check_cake_stock.sql");
        try (Connection conn = DButil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cake);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt("stock") >= qty;
        } catch (SQLException e) {
            return false;
        }
    }

    // 예약 가능한 시간 반복 입력 (공용 유틸 메서드)
    public static int promptPickupTime(Scanner sc, String date, Connection conn) throws SQLException {
        int time;
        while (true) {
            System.out.println("\n[ 픽업 시간 선택 ]\n10시, 11시, ..., 17시");
            System.out.print("픽업 시간을 입력해주세요 (예: 14): ");
            try {
                time = Integer.parseInt(sc.nextLine());


                if (time < 10 || time > 17) {
                    System.out.println("영업 시간(10~17시)만 입력 가능합니다.");
                    continue;
                }

                String sql = SQLLoader.load("check_pickup_availability.sql");
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setDate(1, java.sql.Date.valueOf(date));
                    pstmt.setInt(2, time);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next() && rs.getInt("cnt") == 0) {
                        break;
                    } else {
                        System.out.println("이미 예약된 시간입니다. 다시 선택해주세요.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자만 입력해주세요.");
            }
        }
        return time;
    }
}

