# 🎂 케이크 예약 시스템 (Cake Reservation System)

## 📌 프로젝트 소개
Java 콘솔 기반의 케이크 예약 시스템입니다.  
회원가입부터 로그인, 케이크 선택, 수령일 예약, 예약 변경/취소까지 케이크 매장에서 필요한 전체 예약 흐름을 구현합니다.

---

## 📁 폴더 구조
📦 project-root
```
src/
├── main/
│ ├── java/
│ │ └── com/cake/
│ │ ├── app/ # 시스템 실행 및 초기화
│ │ │ ├── Main.java
│ │ │ └── appMain.java
│ │ ├── service/ # 기능별 서비스 클래스
│ │ │ ├── Login.java
│ │ │ ├── SignUp.java
│ │ │ ├── CakeReservation.java
│ │ │ ├── CancelAccount.java
│ │ │ ├── ChangeReservation.java
│ │ │ ├── CheckUserInfo.java
│ │ │ └── CakePopularityBySize.java
│ │ └── util/ # DB 유틸리티
│ │ ├── DButil.java
│ │ └── SQLLoader.java
│ │
│ └── resources/
│ └── sql/
│ ├── schema/ # DB 초기화용 스크립트
│ │ ├── createschema.sql
│ │ ├── dropschema.sql
│ │ └── initdata.sql
│ └── operation/ # 기능별 SQL 쿼리
│ ├── insert_user.sql
│ ├── login_user.sql
│ ├── select_all_cakes.sql
│ ├── ...
```
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

### 📂 `resources/sql/schema`
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

### 1️⃣ MySQL 서버 실행

- 애플리케이션을 실행하기 전에 **MySQL 서버가 실행 중**이어야 합니다.
- 또한  **'cake_db'  데이터베이스가 사전에 존재**해야 합니다. 만약 해당 데이터베이스가 존재하지 않을 시 “java.sql.SQLSyntaxErrorException: Unknown database 'cake_db'” 에러가 뜨게 됩니다.
- DB 접속 정보는 `DButil.java`에 정의되어 있으며, 비밀번호는 실행 중 **콘솔에서 직접 입력**받습니다.

---

### 2️⃣ 프로그램 실행 (개발용)

```bash
javac com/cake/app/appMain.java
java com.cake.app.appMain
```

appMain을 실행하면 내부적으로 다음이 자동으로 수행됩니다:

- Main.main() 호출 → 데이터베이스 초기화  
  - 기존 스키마 삭제 (dropschema.sql)  
  - 새 스키마 생성 (createschema.sql)  
  - 샘플 데이터 삽입 (initdata.sql)  

- 이 과정 중 **MySQL 비밀번호 입력을 요구하는 콘솔 창이 뜨며**, 입력 후 연결됩니다.  
  - 비밀번호는 콘솔에 표시되지 않으며, 직접 타이핑해야 합니다.  

- 초기화가 완료되면 **콘솔 메뉴가 출력**되어 기능을 선택할 수 있습니다.

---

### 3️⃣ 프로그램 실행 (배포용 JAR 파일 사용 시)

1. 터미널(명령 프롬프트)에서 `.jar` 파일이 위치한 디렉토리로 이동합니다.

```bash
cd [JAR 파일 경로]
```

2. 아래 명령어를 입력해 실행합니다:

```bash
java -jar CakeReservationSystem-1.0-SNAPSHOT.jar
```

- 실행하면 위 개발용과 동일하게 DB 초기화 및 콘솔 메뉴가 표시됩니다.
- 역시 **MySQL 비밀번호를 터미널에서 입력해야 연결이 완료됩니다.**

---

### 4️⃣ 콘솔 기능

- 회원가입 / 로그인  
- 케이크 목록 조회 및 예약  
- 예약 수정 / 취소 / 확인  
- 회원탈퇴  
- 인기 케이크 통계 확인  

---

### ⚠️ 주의사항

- 실행 시 기존 DB가 초기화되므로 **중요 데이터는 백업 후 진행**하세요.  
- DB 연결 실패 시:
  - MySQL 서버가 실행 중인지 확인
  - 비밀번호를 정확히 입력했는지 확인

---

## ✅ 사용 기술

- Java 17+
- JDBC
- MySQL 8+
- SQL 분리 저장 및 동적 실행 (`SQLLoader.java` 사용)

---
