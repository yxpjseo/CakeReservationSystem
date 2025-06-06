
---

## 💡 주요 기능

### 👤 사용자
- 회원가입, 로그인
- 케이크 목록 조회 및 예약
- 예약 상세 확인
- 예약 수정 (초 개수, 픽업 시간 변경)
- 예약 취소
- 회원탈퇴

### 📊 통계
- 케이크 사이즈별 인기 케이크 조회

---

## 🗄️ 데이터베이스

### 테이블
- `users`: 사용자 정보
- `cakes`: 케이크 종류 및 재고
- `orders`: 주문 기본 정보 (총액, 날짜 등)
- `order_items`: 케이크 종류별 수량
- `pick_ups`: 수령 날짜/시간, 상태

### 뷰
- `user_info_for_users`: 사용자 개인정보 요약
- `reservation_status`: 예약 상태 요약

---

## 🧪 SQL 스크립트

### 📂 `resources/schema`
- `createschema.sql`: 테이블 및 뷰 생성
- `dropschema.sql`: 전체 테이블 삭제
- `initdata.sql`: 샘플 유저, 케이크, 예약 데이터 삽입

### 📂 `resources/sql/operation`
- `insert_user.sql`, `login_user.sql`: 로그인 및 회원가입
- `select_all_cakes.sql`, `insert_order.sql` 등: 예약 로직 구성
- `update_cake_stock.sql`, `update_pickup_time.sql`: 예약 변경
- `delete_user.sql`, `delete_order_by_orderId.sql`: 예약 취소 및 탈퇴 처리
- `select_user_reservations.sql`, `select_reservation_detail.sql`: 조회 기능

---

## ▶️ 실행 방법

1. **DB 구성**
    ```bash
    mysql -u [user] -p < schema/dropschema.sql
    mysql -u [user] -p < schema/createschema.sql
    mysql -u [user] -p < schema/initdata.sql
    ```

2. **애플리케이션 실행**
    - `Main.java`: DB 초기화 자동 실행
    - `appMain.java`: 메인 실행 → 콘솔 기반 메뉴 제공

---

## 🔐 샘플 계정

| 이름 | 전화번호 | 비밀번호 |
|------|----------|-----------|
| Olivia | 01012345678 | 7261 |
| Ethan | 01098765432 | 4938 |
| ... | ... | ... |

---

## ✅ 사용 기술

- Java 17+
- JDBC
- MySQL 8+
- SQL 분리 저장 및 동적 실행 (`SQLLoader.java` 사용)

---
