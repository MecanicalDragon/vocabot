ALTER TABLE subscriptions
    ADD COLUMN last_word_id INT NOT NULL DEFAULT 1;
-- ALTER TABLE subscriptions
--     ADD COLUMN to_learn JSONB NOT NULL DEFAULT '[]'::jsonb;
ALTER TABLE subscriptions
    ADD COLUMN to_learn VARCHAR NOT NULL DEFAULT '[]';
