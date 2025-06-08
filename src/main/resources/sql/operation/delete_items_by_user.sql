DELETE oi
FROM order_items oi
         JOIN orders o ON oi.order_id = o.order_id
WHERE o.user_id = ?;