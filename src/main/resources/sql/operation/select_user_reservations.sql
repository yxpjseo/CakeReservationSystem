-- 사용자의 예약 중 status = false 인 예약 목록 조회
SELECT o.order_id, u.user_name, oi.cake_name, oi.count, o.candles,
       p.pickup_date, p.pickup_time
FROM orders o
         JOIN users u ON o.user_id = u.user_id
         JOIN order_items oi ON o.order_id = oi.order_id
         JOIN pick_ups p ON o.order_id = p.order_id
WHERE o.user_id = ? AND p.status = FALSE
ORDER BY o.order_id;
