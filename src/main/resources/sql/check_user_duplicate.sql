-- 중복 확인
SELECT COUNT(*)
FROM users
WHERE phone_num = ? OR email = ?;