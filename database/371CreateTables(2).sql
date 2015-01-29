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
DROP DOMAIN IF EXISTS restaruant, address, postcode, city, province, rha, insDate, insType, insPriority, violationId, violationDes, violationPriority, rate, categoryId, categoryDes;

CREATE DOMAIN restaruant varchar(1000);
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
    locationrestaruant restaruant NOT NULL,
    locationAddress address NOT NULL,
    locationPostcode postcode NOT NULL,
    locationCity city NOT NULL,
    locationProvince province NOT NULL,
    locationRHA rha NOT NULL,
    PRIMARY KEY (locationrestaruant, locationAddress, locationPostcode)
);

  
CREATE TABLE inspection(
    inspectionrestaruant restaruant NOT NULL,
    inspectionAddress address NOT NULL,
    inspectionPostcode postcode NOT NULL,
    inspectionInsDate insDate NOT NULL,
    inspectionInsType insType NOT NULL,
    inspectionInsPriority insPriority NOT NULL,
    PRIMARY KEY (inspectionrestaruant, inspectionAddress, inspectionPostcode, inspectionInsDate),
    FOREIGN KEY (inspectionrestaruant, inspectionAddress, inspectionPostcode)
        REFERENCES location
            ON UPDATE CASCADE
            ON DELETE CASCADE
);


CREATE TABLE violation_types(
    vioViolationId violationId NOT NULL PRIMARY KEY,
    vioViolationDes violationDes NOT NULL,
    vioViolationPriority ViolationPriority NOT NULL
);


CREATE TABLE violations(
    viorestaruant restaruant NOT NULL,
    vioAddress address NOT NULL,
    vioPostcode postcode NOT NULL,
    vioInsDate insDate NOT NULL,
    vioViolationId violationId NOT NULL,
    PRIMARY KEY (viorestaruant, vioAddress, vioPostcode, vioInsDate, vioViolationId),
    FOREIGN KEY (viorestaruant, vioAddress, vioPostcode, vioInsDate)
        REFERENCES inspection
            ON UPDATE CASCADE
            ON DELETE CASCADE,
    FOREIGN KEY (vioViolationId)
        REFERENCES violation_types
            ON UPDATE CASCADE
            ON DELETE CASCADE
);


CREATE TABLE rating(
    ratingrestaruant restaruant NOT NULL,
    ratingAddress address NOT NULL,
    ratingPostcode postcode NOT NULL,
    ratingrate rate NOT NULL,
    PRIMARY KEY (ratingrestaruant, ratingAddress, ratingPostcode),
    FOREIGN KEY (ratingrestaruant, ratingAddress, ratingPostcode)
        REFERENCES location
            ON UPDATE CASCADE
            ON DELETE CASCADE
);


CREATE TABLE category_description(
    cdCategoryId categoryId NOT NULL PRIMARY KEY,
    cdCategoryDes categoryDes NOT NULL
);


CREATE TABLE categories(
    caterestaruant restaruant NOT NULL,
    cateAddress address NOT NULL,
    catePostcode postcode NOT NULL,
    cateCategoryId categoryId NOT NULL,
    PRIMARY KEY (caterestaruant, cateAddress, catePostcode),
    FOREIGN KEY (caterestaruant, cateAddress, catePostcode)
        REFERENCES location
            ON UPDATE CASCADE
            ON DELETE CASCADE,
    FOREIGN KEY (cateCategoryId)
        REFERENCES category_description
            ON UPDATE CASCADE
            ON DELETE CASCADE
);

