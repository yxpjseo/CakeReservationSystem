-- 단일 주문 상세 정보 조회 (케이크, 초, 픽업 일시)
SELECT cake_name, count, candles, pickup_date, pickup_time
FROM reservation_status
WHERE order_id=?;