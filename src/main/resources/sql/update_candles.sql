-- 초 수량 변경
UPDATE orders
SET candles = ?
WHERE order_id = ?;