CREATE TABLE irregular
(
    id      SERIAL,
    form1   VARCHAR  NOT NULL,
    form2   VARCHAR  NOT NULL,
    form3   VARCHAR  NOT NULL,
    pattern SMALLINT NOT NULL,
    description VARCHAR NOT NULL DEFAULT ''
);
