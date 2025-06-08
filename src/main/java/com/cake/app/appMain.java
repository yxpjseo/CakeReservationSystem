package com.cake.app;

import com.cake.app.*;
import com.cake.service.*;
import com.cake.util.DButil;

import java.sql.Connection;
import java.util.Scanner;

public class appMain {



    // 애플리케이션 실행 시 가장 먼저 뜨는 메뉴
    public static void main(String[] args) {
        Main.main(args);
        Scanner sc = new Scanner(System.in);
        SignUp signUp = new SignUp();
        Login login = new Login();
        CakeReservation reservation = new CakeReservation();
        CakePopularityBySize popularity = new CakePopularityBySize();
        CancelAccount cancelAccount = new CancelAccount();
        CheckUserInfo checkUserInfo = new CheckUserInfo();
        ChangeReservation changeReservation = new ChangeReservation();

        while (true) {
            System.out.println("\n===== 🍰 케이크 예약 시스템 =====");
            System.out.println("1. 회원가입");
            System.out.println("2. 로그인");
            System.out.println("0. 종료");
            System.out.print("선택 >> ");
            String input = sc.nextLine().trim();

            switch (input) {
                case "1" -> {
                    try (Connection conn = DButil.getConnection()) {
                        signUp.registerUser(conn, sc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "2" -> {
                    try (Connection conn = DButil.getConnection()) {
                        int userId = login.loginUser(conn, sc);
                        if (userId > 0) {
                            loginMenu(sc, userId, reservation, popularity, cancelAccount, checkUserInfo, changeReservation);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "0" -> {
                    System.out.println("이용해주셔서 감사합니다. 안녕히 가세요!");
                    return;
                }
                default -> System.out.println("❌ 잘못된 입력입니다.");
            }
        }
    }

    // 로그인한 사용자에게 보이는 메뉴
    private static void loginMenu(Scanner sc, int userId,
                                  CakeReservation reservation,
                                  CakePopularityBySize popularity,
                                  CancelAccount cancelAccount,
                                  CheckUserInfo checkUserInfo,
                                  ChangeReservation changeReservation) {

        while (true) {
            System.out.println("\n===== 🍓 메뉴 =====");
            System.out.println("1. 예약하기");
            System.out.println("2. 사이즈별 인기 케이크 조회");
            System.out.println("3. 내 예약 현황 보기 / 변경 / 취소");
            System.out.println("4. 내 정보 확인");
            System.out.println("5. 회원 탈퇴");
            System.out.println("0. 로그아웃");
            System.out.print("선택 >> ");
            String menu = sc.nextLine().trim();

            try (Connection conn = DButil.getConnection()) {
                switch (menu) {
                    case "1" -> reservation.execute(sc, userId);
                    case "2" -> popularity.execute(sc);
                    case "3" -> changeReservation.showUserReservations(conn, userId, sc);
                    case "4" -> checkUserInfo.showUserInfo(conn, userId);
                    case "5" -> {
                        cancelAccount.deleteUserAccount(conn, sc, userId);
                        return;
                    }
                    case "0" -> {
                        System.out.println("로그아웃 되었습니다.");
                        return;
                    }
                    default -> System.out.println("❌ 잘못된 입력입니다.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
