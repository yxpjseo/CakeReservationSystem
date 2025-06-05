SELECT *
FROM order_completed
WHERE user_id = ? AND status = FALSE
ORDER BY order_id;