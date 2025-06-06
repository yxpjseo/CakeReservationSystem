SELECT c.cake_name, SUM(oi.count) AS total_ordered
FROM cakes c
         JOIN order_items oi ON c.cake_name = oi.cake_name
WHERE c.size = ?
GROUP BY c.cake_name
ORDER BY total_ordered DESC
LIMIT 5;