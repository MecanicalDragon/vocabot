ALTER TABLE subscriptions ADD CONSTRAINT sub_unique_id UNIQUE (subscription_id);
ALTER TABLE vocabulary ADD CONSTRAINT word_unique_id UNIQUE (id);
CREATE TABLE IF NOT EXISTS learnings(
    id SERIAL,
    sub_id BIGINT NOT NULL,
    word_id BIGINT NOT NULL,
    FOREIGN KEY(sub_id) REFERENCES subscriptions(subscription_id),
    FOREIGN KEY(word_id) REFERENCES vocabulary(id)
);
