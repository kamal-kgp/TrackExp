-- schema.sql
CREATE TABLE IF NOT EXISTS messages (
    id      VARCHAR(60)  DEFAULT gen_random_uuid() PRIMARY KEY,
    text    VARCHAR      NOT NULL
    );