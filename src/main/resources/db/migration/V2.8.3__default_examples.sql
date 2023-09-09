ALTER TABLE vocabulary
    ALTER COLUMN examples SET DEFAULT '[]'::jsonb;
