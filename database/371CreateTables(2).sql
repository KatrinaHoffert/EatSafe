 -- For cmpt371 project: EatSafe 
 -- this file creates tables
begin;
    -- drop tables first
DROP TABLE IF EXISTS location CASCADE;
DROP TABLE IF EXISTS inspection CASCADE;
DROP TABLE IF EXISTS violations CASCADE;
DROP TABLE IF EXISTS violation_types CASCADE;
DROP TABLE IF EXISTS rating CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS category_description CASCADE;

    -- drop domain
DROP DOMAIN IF EXISTS restaruant_name, address, postcode, city, province, rha, insDate, insType, insPriority, violationId, violationDes, violationPriority, rate, categoryId, categoryDes;

    -- create domain

CREATE DOMAIN restaruant_name varchar(1000);
CREATE DOMAIN address varchar(1000);
CREATE DOMAIN postcode varchar(7);
CREATE DOMAIN city varchar(100);
CREATE DOMAIN province varchar(100);
CREATE DOMAIN rha varchar(100);

CREATE DOMAIN insDate date;
CREATE DOMAIN insType varchar(100);
CREATE DOMAIN insPriority varchar(100);

CREATE DOMAIN violationId smallint;

CREATE DOMAIN violationDes varchar(1000);
CREATE DOMAIN violationPriority varchar(100);

CREATE DOMAIN rate varchar(10);

CREATE DOMAIN categoryId smallint;

CREATE DOMAIN categoryDes varchar(1000);

CREATE TABLE location(
    ID SERIAL PRIMARY KEY,
    location_restaruant_name restaruant_name NOT NULL,
    location_Address address NOT NULL,
    location_Postcode postcode NOT NULL,
    location_City city NOT NULL,
    location_Province province NOT NULL,
    location_RHA rha NOT NULL
);

  
CREATE TABLE inspection(
    ID int NOT NULL,
    inspection_Date insDate NOT NULL,
    inspection_Type insType NOT NULL,
    Reinspection_Priority insPriority NOT NULL,
    PRIMARY KEY (ID, inspection_Date),
    FOREIGN KEY (ID)
        REFERENCES location
            ON UPDATE CASCADE
            ON DELETE CASCADE
);


CREATE TABLE violation_types(
    Violation_Id violationId NOT NULL PRIMARY KEY,
    Violation_Des violationDes NOT NULL,
    Violation_Priority ViolationPriority NOT NULL
);


CREATE TABLE violations(
    ID int NOT NULL,
    violation_Date insDate NOT NULL,
    Violation_Id violationId NOT NULL,
    PRIMARY KEY (ID, violation_Date, Violation_Id),
    FOREIGN KEY (ID, violation_Date)
        REFERENCES inspection
            ON UPDATE CASCADE
            ON DELETE CASCADE,
    FOREIGN KEY (violation_Id)
        REFERENCES violation_types
            ON UPDATE CASCADE
            ON DELETE CASCADE
);


CREATE TABLE rating(
    ID int NOT NULL,
    rating rate NOT NULL,
    PRIMARY KEY (ID, rating),
    FOREIGN KEY (ID)
        REFERENCES location
            ON UPDATE CASCADE
            ON DELETE CASCADE
);


CREATE TABLE category_description(
    Category_Id categoryId NOT NULL PRIMARY KEY,
    Category_Des categoryDes NOT NULL
);


CREATE TABLE categories(
    ID int NOT NULL,
    Category_Id categoryId NOT NULL,
    PRIMARY KEY (ID, Category_Id),
    FOREIGN KEY (ID)
        REFERENCES location
            ON UPDATE CASCADE
            ON DELETE CASCADE,
    FOREIGN KEY (category_Id)
        REFERENCES category_description
            ON UPDATE CASCADE
            ON DELETE CASCADE
);

