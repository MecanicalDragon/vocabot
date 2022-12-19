ALTER TABLE vocabulary ADD COLUMN examples_temp JSONB;
UPDATE vocabulary SET examples_temp = to_jsonb(regexp_split_to_array(vocabulary.examples, '\|')) WHERE vocabulary.examples IS NOT NULL;
ALTER TABLE vocabulary RENAME COLUMN examples TO examples_plain;
ALTER TABLE vocabulary RENAME COLUMN examples_temp TO examples;
