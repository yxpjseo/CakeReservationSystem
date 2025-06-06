-- 픽업시간 변경
UPDATE pick_ups
SET pickup_time = ?
WHERE order_id = ?;