DROP TABLE IF EXISTS location_search CASCADE;

CREATE TABLE location_search AS
    SELECT id AS location_id, to_tsvector('english', concat_ws(' ', name, address)) AS text_vector
    FROM location;