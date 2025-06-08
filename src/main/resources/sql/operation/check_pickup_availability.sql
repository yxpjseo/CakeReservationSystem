-- 픽업 시간 중복 확인
SELECT COUNT(*) AS cnt
FROM pick_ups
WHERE pickup_date = ? AND pickup_time = ?;
