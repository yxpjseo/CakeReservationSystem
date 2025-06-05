-- VIEW cakes_total_ordered 이용
SELECT cake_name, total_ordered
FROM cakes_total_ordered
WHERE size = ?
ORDER BY total_ordered DESC
LIMIT 5;