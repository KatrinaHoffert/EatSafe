 -- EatSafe 
 -- create tables
 -- drop tables first
DROP TABLE IF EXISTS location CASCADE;
DROP TABLE IF EXISTS inspection CASCADE; 
DROP TABLE IF EXISTS violation CASCADE;
DROP TABLE IF EXISTS violation_type CASCADE;
DROP TABLE IF EXISTS coordinates CASCADE;

CREATE TABLE location(
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    address TEXT,
    postcode TEXT,
    city TEXT,
    rha TEXT NOT NULL
);

  
CREATE TABLE inspection(
    id SERIAL PRIMARY KEY,
    location_id INT NOT NULL,
    inspection_date DATE NOT NULL,
    inspection_type TEXT NOT NULL,
    reinspection_priority TEXT NOT NULL,
    FOREIGN KEY (location_id)
        REFERENCES location
            ON UPDATE NO ACTION
            ON DELETE CASCADE
);


CREATE TABLE violation_type(
    id INT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    priority TEXT NOT NULL
);


CREATE TABLE violation(
    inspection_id INT NOT NULL,
    violation_id INT NOT NULL,
    PRIMARY KEY (inspection_id, violation_id),
    FOREIGN KEY (inspection_id)
        REFERENCES inspection
            ON UPDATE NO ACTION
            ON DELETE CASCADE,
    FOREIGN KEY (violation_Id)
        REFERENCES violation_type
            ON UPDATE NO ACTION
            ON DELETE CASCADE
);


CREATE TABLE coordinates(
    location_id INT NOT NULL,
    latitude DECIMAL(10,6),
    longitude DECIMAL(10,6)
);