UPDATE vocabulary SET examples = '[]'::jsonb where examples is null;
ALTER TABLE vocabulary ALTER COLUMN examples SET NOT NULL;
