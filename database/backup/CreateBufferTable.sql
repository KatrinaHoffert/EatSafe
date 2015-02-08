 -- EatSafe 
 -- create the import buffer table

    -- since the attributes in the CSV is named as textboxN, this file is uesd to document what those attributes refer to. 

    -- drop tables first
DROP TABLE IF EXISTS importData CASCADE;


CREATE TABLE importData (
    textbox1 importChar,
    textbox2 importChar,
    textbox3 importChar,
    textbox4 importChar,
    textbox5 importChar,
    textbox6 importChar,    -- the datetime when the report downloaded
    textbox7 importChar,
    textbox8 importChar,
    textbox9 importChar,    -- violation priority
    textbox10 importChar,   -- name of location
    textbox11 importChar,
    textbox12 importChar,
    textbox13 importChar,
    textbox14 importChar,
    textbox15 importChar,
    textbox16 importChar,   -- violation
    textbox17 importChar,   -- address
    textbox18 importChar,
    textbox19 importChar,
    textbox20 importChar,   -- in format of "CITYNAME, PROVINCENAME,    ABC 123" 
    textbox21 importChar,
    textbox22 importChar,
    textbox23 importChar,   -- rha
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
    textbox35 importChar,   -- date of inspection
    textbox36 importChar,
    textbox37 importChar,
    textbox38 importChar,
    textbox39 importChar,   -- reinspection priority
    textbox40 importChar,
    textbox41 importChar,
    textbox42 importChar,
    textbox43 importChar,   -- inspection type
    textbox44 importChar,
    textbox45 importChar,
    textbox46 importChar,
    textbox47 importChar,
    textbox48 importChar,
    textbox49 importChar,
    textbox50 importChar
);

    