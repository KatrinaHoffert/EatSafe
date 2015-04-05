-- Remove existing objects that we're about to create to prevent conflicts.
DROP TABLE IF EXISTS location_search CASCADE;
DROP TEXT SEARCH CONFIGURATION IF EXISTS eatsafe_english CASCADE;
DROP TEXT SEARCH DICTIONARY IF EXISTS eatsafe_synonyms CASCADE;

-- We need to apply our own synonyms dictionary so that users can, for example, get results like
-- "8th st" when they search for "8th street". Doing so requires that we copy the english text
-- search configuration because we can't alter english's configuration with an unprivileged user.
CREATE TEXT SEARCH CONFIGURATION eatsafe_english (
  COPY = english
);

CREATE TEXT SEARCH DICTIONARY eatsafe_synonyms (
  TEMPLATE = synonym,
  SYNONYMS = eatsafe_synonyms
);

-- See <http://stackoverflow.com/a/2227235/1968462>
CREATE TEXT SEARCH DICTIONARY eatsafe_stem_no_stop_words (
  Template = snowball,
  Language = english
);

ALTER TEXT SEARCH CONFIGURATION eatsafe_english
  ALTER MAPPING FOR asciiword, asciihword, hword_asciipart, hword, hword_part, word
  WITH eatsafe_synonyms, eatsafe_stem_no_stop_words;

-- The location_search table maps TSVECTORS (which contain lexemes) to location IDs (so we can figure
-- out what location we matched). The location name and address are both used in the TSVECTOR, with
-- the name being weighted heavier. Note that since the address might be null, we must provide a
-- default (an empty TSVECTOR), as the concatenation of TSVECTORS will fail if we try and concat
-- a NULL.
CREATE TABLE location_search AS
  SELECT id AS location_id, setweight(to_tsvector('eatsafe_english', name), 'A')
      || COALESCE(setweight(to_tsvector('eatsafe_english', address), 'B'),
      to_tsvector('eatsafe_english', '')) AS text_vector
  FROM location;