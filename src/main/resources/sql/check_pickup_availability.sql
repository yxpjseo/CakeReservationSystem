SELECT COUNT(*) AS cnt
FROM pick_ups
WHERE pickup_date = ? AND pickup_time = ?;
