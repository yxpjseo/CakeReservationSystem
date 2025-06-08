-- orders 테이블에 주문 정보 삽입
INSERT INTO orders (user_id, tot_price, order_date, candles)
VALUES (?, ?, ?, ?);