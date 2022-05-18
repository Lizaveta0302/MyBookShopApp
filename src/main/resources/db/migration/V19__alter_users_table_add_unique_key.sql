ALTER TABLE shop.users
    DROP CONSTRAINT email_unique_key;
ALTER TABLE shop.users
    ADD CONSTRAINT email_unique_key UNIQUE (email);