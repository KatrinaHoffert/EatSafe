 -- For cmpt371 project: EatSafe 
 -- this file insert data from importDate (buffer table)
begin;


INSERT INTO location (location_restaurant_name, location_Address, location_Postcode, location_City, location_Province, location_RHA)
SELECT DISTINCT initcap(textbox10), textbox17, right(textbox20, 7), 'Saskatoon', 'Saskatchewan', 'Saskatoon Health Authority' FROM importData;


INSERT INTO inspection (ID, inspection_Date, inspection_Type, reinspection_Priority) 
SELECT DISTINCT ID, to_date(textbox35, 'YYYY MM DD'), textbox43, textbox39 
FROM importData, location 
WHERE initcap(textbox10) = location_restaurant_name AND textbox17 = location_address AND right(textbox20, 7) = location_Postcode;

INSERT INTO violation_types (Violation_Id, Violation_Des,  Violation_Priority) 
SELECT DISTINCT CAST(left(textbox16, 2) AS smallint), textbox16, textbox9 FROM importData WHERE textbox16 NOT IN ('');

INSERT INTO violations (ID, violation_Date, violation_Id) 
SELECT ID, to_date(textbox35, 'YYYY MM DD'), CAST(left(textbox16, 2) AS smallint) 
FROM importData, location WHERE textbox16 NOT IN ('') AND initcap(textbox10) = location_restaurant_name AND textbox17 = location_address AND right(textbox20, 7) = location_Postcode;;