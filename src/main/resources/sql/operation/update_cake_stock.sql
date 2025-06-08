-- 케이크 재고 변경
UPDATE cakes SET stock = stock - ? WHERE cake_name = ?;