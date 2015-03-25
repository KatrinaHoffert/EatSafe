DROP TABLE IF EXISTS location_search CASCADE;
DROP TEXT SEARCH CONFIGURATION IF EXISTS eatsafe_english CASCADE;
DROP TEXT SEARCH DICTIONARY IF EXISTS eatsafe_synonyms CASCADE;

CREATE TEXT SEARCH CONFIGURATION eatsafe_english (
  COPY = english
);

CREATE TEXT SEARCH DICTIONARY eatsafe_synonyms (
  TEMPLATE = synonym,
  SYNONYMS = eatsafe_synonyms
);

ALTER TEXT SEARCH CONFIGURATION eatsafe_english
  ALTER MAPPING FOR asciiword
  WITH eatsafe_synonyms, english_stem;

CREATE TABLE location_search AS
  SELECT id AS location_id, to_tsvector('eatsafe_english', concat_ws(' ', name, address)) AS text_vector
  FROM location;