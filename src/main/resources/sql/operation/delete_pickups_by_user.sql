DELETE p
FROM pick_ups p
         JOIN orders o ON p.order_id = o.order_id
WHERE o.user_id = ?;