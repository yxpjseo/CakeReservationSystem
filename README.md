# 🎂 케이크 예약 시스템 (Java + MySQL)

## 📌 소개
이 프로젝트는 케이크 매장의 **온라인 예약 및 주문 시스템**을 Java 콘솔 기반으로 구현한 프로그램입니다. 사용자는 회원가입 후 로그인을 통해 케이크를 예약하고, 예약 내역을 조회 및 수정/취소할 수 있습니다.

## 🛠️ 기술 스택
- Java (콘솔 기반)
- JDBC
- MySQL
- SQL 스크립트 (`.sql` 파일)

---

## 📁 프로젝트 구조

| 파일명 | 역할 |
|--------|------|
| `Main.java` | DB 초기화 실행: 테이블/뷰 삭제 및 생성, 초기 데이터 삽입 |
| `appMain.java` | 프로그램 진입점. 로그인/회원가입 및 사용자 메뉴 처리 |
| `Login.java` | 사용자 로그인 기능 |
| `SignUp.java` | 사용자 회원가입 기능 |
| `CakeReservation.java` | 케이크 예약 기능: 상품 선택 → 장바구니 → 수령일/시간 지정 → 주문 저장 |
| `CakePopularityBySize.java` | 케이크 크기별 인기 통계 출력 |
| `CancelAccount.java` | 회원 탈퇴 처리 |
| `ChangeReservation.java` | 예약 수정 처리 (날짜 및 시간 변경) |
| `CheckUserInfo.java` | 사용자 개인정보 출력 |
| `DButil.java` | DB 연결 및 종료 유틸리티 |
| `SQLLoader.java` | SQL 파일(`.sql`) 실행 도우미 |
| `createschema.sql` | DB 테이블 및 뷰 생성 |
| `dropschema.sql` | DB 테이블 및 뷰 삭제 |
| `initdata.sql` | 샘플 데이터 삽입 |

---

## 🧾 주요 기능

### ✅ 사용자 기능
- 회원가입 / 로그인
- 케이크 예약 (상품 선택, 수령일 지정)
- 예약 조회, 수정, 취소
- 개인정보 확인
- 회원 탈퇴

### 📊 관리자 기능 (일부 포함)
- 케이크 사이즈별 인기순 통계 확인 (`CakePopularityBySize.java`)

---

## 🗄️ 데이터베이스 구조

### 📌 테이블 요약
- `users` : 사용자 정보
- `cakes` : 케이크 정보
- `orders` : 주문 정보
- `order_items` : 주문 상세 (케이크별 수량)
- `pick_ups` : 예약 수령일 및 시간
- `user_info_for_users` (VIEW): 사용자 개인정보 조회용
- `reservation_status` (VIEW): 예약 내역 확인용

---

## 🚀 실행 방법

1. MySQL에 `createschema.sql`, `initdata.sql` 실행하여 DB 초기화
2. `Main.java` 실행 → DB 자동 초기화
3. `appMain.java` 실행 → 콘솔 메뉴에서 기능 선택

---

## 📝 초기 계정 정보

초기 DB에는 10명의 사용자가 등록되어 있습니다. 예:

- **이름**: Olivia  
- **전화번호**: `01012345678`  
- **비밀번호**: `7261`

---

## 📌 주의사항
- 수령시간은 10~19시 사이에서 지정 가능합니다.
- 비밀번호는 4자리 숫자 형식입니다.
- 같은 시간대에 중복 주문 시 재고가 차감되며, 부족하면 주문이 불가합니다.

---
