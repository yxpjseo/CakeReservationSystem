-- 기존 테이블 삭제
DROP TABLE IF EXISTS pick_ups;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cakes;
DROP TABLE IF EXISTS users;

-- 뷰 삭제
DROP VIEW IF EXISTS user_info_for_users;
DROP VIEW IF EXISTS cakes_total_ordered;


CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY, -- id의 번호는 자동증가
                       user_name VARCHAR(30),
                        phone_num CHAR(13),
                        email VARCHAR(30),
                        pw char(4)
    );

CREATE TABLE cakes (
                       cake_name VARCHAR(30) PRIMARY KEY,
                       size INT CHECK (size IN (1, 2, 3)), -- 사이즈는 1,2,3 으로만 제한함
                       price INT,
                       stock INT
);

CREATE TABLE orders (
                        order_id INT AUTO_INCREMENT PRIMARY KEY, -- order_id의 번호는 자동 증가
                        user_id INT,
                        tot_price INT,
                        order_date DATE,
                        candles INT CHECK (candles BETWEEN 0 AND 100), -- 초는 0부터 100까지 제한함
                        FOREIGN KEY (user_id) REFERENCES users(user_id)
    );

CREATE TABLE order_items (
                             order_id INT,
                             cake_name VARCHAR(30),
                             count INT,
                             PRIMARY KEY (order_id, cake_name),
                             FOREIGN KEY (order_id) REFERENCES orders(order_id),
                             FOREIGN KEY (cake_name) REFERENCES cakes(cake_name)
);

CREATE TABLE pick_ups (
                          pickup_id INT AUTO_INCREMENT PRIMARY KEY, -- pickup_id의 번호는 자동 증가
                          pickup_date DATE, -- built-in data type 'date' 사용
                          pickup_time INT CHECK (pickup_time BETWEEN 10 AND 19),
    order_id INT,
    status BOOLEAN,
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
    );


-- VIEW

-- pw를 제외한 회원정보 확인용 VIEW
-- 사용자에게 노출 가능한 기본 회원정보 View
CREATE VIEW user_info_for_users AS
SELECT user_id, user_name, phone_num, email
FROM users;

-- 누적 판매량 VIEW
-- 각 케이크별 누적 판매량 집계 View
CREATE OR REPLACE VIEW cakes_total_ordered AS
SELECT c.cake_name, c.size, SUM(oi.count) AS total_ordered
FROM cakes c
         JOIN order_items oi ON c.cake_name = oi.cake_name
GROUP BY c.cake_name, c.size;

-- size 인덱스
CREATE INDEX idx_cake_size ON cakes(size);


