-- 주문 상세 정보 조회 (케이크, 초, 픽업 일시)
SELECT oi.cake_name, oi.count, o.candles,
       p.pickup_date, p.pickup_time
FROM order_items oi
         JOIN orders o ON oi.order_id = o.order_id
         JOIN pick_ups p ON o.order_id = p.order_id
WHERE o.order_id = ?;