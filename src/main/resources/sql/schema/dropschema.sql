-- Drop Views
DROP VIEW IF EXISTS cakes_total_ordered;
DROP VIEW IF EXISTS user_info_for_users;

-- Drop Index 
DROP INDEX IF EXISTS idx_cake_size ON cakes;

-- Drop Tables 
DROP TABLE IF EXISTS pick_ups;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cakes;
DROP TABLE IF EXISTS users;
