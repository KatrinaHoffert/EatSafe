 -- For cmpt371 project: EatSafe 
 -- this file creates the import buffer table

    -- drop tables first
DROP TABLE IF EXISTS importData CASCADE;

    -- drop domain
DROP DOMAIN IF EXISTS importChar;

CREATE DOMAIN importChar varchar(1000);



CREATE TABLE importData (
    textbox1 importChar,
    textbox2 importChar,
    textbox3 importChar,
    textbox4 importChar,
    textbox5 importChar,
    textbox6 importChar,
    textbox7 importChar,
    textbox8 importChar,
    textbox9 importChar,
    textbox10 importChar,
    textbox11 importChar,
    textbox12 importChar,
    textbox13 importChar,
    textbox14 importChar,
    textbox15 importChar,
    textbox16 importChar,
    textbox17 importChar,
    textbox18 importChar,
    textbox19 importChar,
    textbox20 importChar,
    textbox21 importChar,
    textbox22 importChar,
    textbox23 importChar,
    textbox24 importChar,
    textbox25 importChar,
    textbox26 importChar,
    textbox27 importChar,
    textbox28 importChar,
    textbox29 importChar,
    textbox30 importChar,
    textbox31 importChar,
    textbox32 importChar,
    textbox33 importChar,
    textbox34 importChar,
    textbox35 importChar,
    textbox36 importChar,
    textbox37 importChar,
    textbox38 importChar,
    textbox39 importChar,
    textbox40 importChar,
    textbox41 importChar,
    textbox42 importChar,
    textbox43 importChar,
    textbox44 importChar,
    textbox45 importChar,
    textbox46 importChar,
    textbox47 importChar,
    textbox48 importChar,
    textbox49 importChar,
    textbox50 importChar
);

    