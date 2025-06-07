-- Users Table
CREATE TABLE users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,  -- id 자동 증가
  user_name VARCHAR(30),
  phone_num CHAR(11),
  email VARCHAR(30),
  pw CHAR(4)
);

-- Cakes Table
CREATE TABLE cakes (
  cake_name VARCHAR(30) PRIMARY KEY,
  size INT CHECK (size IN (1, 2, 3)),  -- 1호, 2호, 3호만 허용
  price INT,
  stock INT
);

-- Orders Table
CREATE TABLE orders (
  order_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  tot_price INT,
  order_date DATE,
  candles INT CHECK (candles BETWEEN 0 AND 100),  -- 0~100개 제한
  FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Order Items Table (다대다 관계 테이블)
CREATE TABLE order_items (
  order_id INT,
  cake_name VARCHAR(30),
  count INT,
  PRIMARY KEY (order_id, cake_name),
  FOREIGN KEY (order_id) REFERENCES orders(order_id),
  FOREIGN KEY (cake_name) REFERENCES cakes(cake_name)
);

-- Pick Ups Table
CREATE TABLE pick_ups (
  pickup_id INT AUTO_INCREMENT PRIMARY KEY,
  pickup_date DATE,
  pickup_time INT CHECK (pickup_time BETWEEN 10 AND 19),  -- 11시~19시
  order_id INT,
  status BOOLEAN,
  FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

-- View: 사용자 정보 요약
CREATE VIEW user_info_for_users AS
SELECT user_id, user_name, phone_num, email
FROM users;

-- View: 예약 확인
CREATE OR REPLACE VIEW reservation_status AS
SELECT
    o.user_id, o.order_id, u.user_name,
    oi.cake_name, oi.count, o.candles,
    p.pickup_date, p.pickup_time, p.status
FROM orders o
         JOIN users u ON o.user_id = u.user_id
         JOIN order_items oi ON o.order_id = oi.order_id
         JOIN pick_ups p ON o.order_id = p.order_id;


-- Indexes
-- 회원가입 시 중복 확인, 로그인 시 조회
CREATE UNIQUE INDEX idx_user_identity ON users(phone_num, email);
-- 사용자별 주문 내역 조회
CREATE INDEX idx_orders_user_id ON orders(user_id);
-- 픽업시간 중복 여부 확인
CREATE INDEX idx_pickup_datetime ON pick_ups(pickup_date, pickup_time);