ALTER TABLE shop.books ALTER is_bestseller TYPE bool USING CASE WHEN is_bestseller=0 THEN FALSE ELSE TRUE END;
ALTER TABLE shop.books ALTER COLUMN is_bestseller SET DEFAULT FALSE;