alter table shop.balance_transaction add is_income bool default false NOT NULL;
alter table shop.balance_transaction alter column book_id DROP NOT NULL;

