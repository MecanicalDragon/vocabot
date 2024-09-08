CREATE TABLE idioms
(
    id       SERIAL,
    idiom    VARCHAR NOT NULL,
    meaning  VARCHAR NOT NULL,
--     examples JSONB DEFAULT '[]'::jsonb
    examples VARCHAR NOT NULL DEFAULT '[]'
);
