package com.cake.service;

import java.sql.*;
import java.util.Scanner;
import com.cake.util.*;

public class CakePopularityBySize {
    
    // 사이즈 별 케이크 인기 순위 조회
    public void execute(Scanner sc) {
        System.out.println("\n// 2. 사이즈 별 케이크 인기순위");
        System.out.println("원하시는 size를 입력해주세요.(숫자만 입력)");
        System.out.println("- 1호\n- 2호\n- 3호");
        System.out.print(">> ");
        int size = Integer.parseInt(sc.nextLine());

        String sql = SQLLoader.load("select_popular_cakes_by_size.sql");

        try (Connection conn = DButil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, size);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n[ " + size + "호 케이크 인기순위 ]");

            int rank = 1;
            while (rs.next()) {
                String cake = rs.getString("cake_name");
                System.out.println(rank++ + ". " + cake);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
