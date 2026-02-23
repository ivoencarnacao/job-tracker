\connect jobtracker_db;

CREATE SCHEMA IF NOT EXISTS core;

DO $$
BEGIN
    RAISE NOTICE '✅ Schema [core] criado com sucesso em jobtracker_db.';
END
$$;