-- VIEW reservation_status 사용
SELECT order_id, user_name, cake_name, count, candles, pickup_date, pickup_time
FROM reservation_status
WHERE user_id = ? AND status = FALSE
ORDER BY order_id;
