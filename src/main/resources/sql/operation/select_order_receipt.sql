SELECT
    oi.cake_name,
    oi.count,
    c.price
FROM order_items oi
         JOIN cakes c ON oi.cake_name = c.cake_name
WHERE oi.order_id = ?;
