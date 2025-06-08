-- 해당 사용자의 모든 주문 삭제
DELETE FROM orders WHERE user_id = ?;