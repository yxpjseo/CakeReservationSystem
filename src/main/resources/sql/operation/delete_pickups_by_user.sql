-- 해당 사용자의 모든 픽업 정보 삭제
DELETE p
FROM pick_ups p
         JOIN orders o ON p.order_id = o.order_id
WHERE o.user_id = ?;