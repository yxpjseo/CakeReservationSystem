-- order_items 테이블에 주문 상세 내역 삽입
INSERT INTO order_items (order_id, cake_name, count)
VALUES (?, ?, ?);