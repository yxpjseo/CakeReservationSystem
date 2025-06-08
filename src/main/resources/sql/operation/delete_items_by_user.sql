-- 해당 사용자의 모든 주문 상세 내역 삭제
DELETE oi
FROM order_items oi
         JOIN orders o ON oi.order_id = o.order_id
WHERE o.user_id = ?;