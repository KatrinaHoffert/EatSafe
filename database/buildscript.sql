Begin;

--Drops the tables and recreates them empty
\i 371CreateTables(2).sql
\i 371CreateTables.sql

-- Takes the various table files "inspection", "location" and loads them into the tables
\i 371CopyIn.sql


