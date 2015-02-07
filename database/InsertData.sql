 -- EatSafe 
 -- insert data from importDate (buffer table)


INSERT INTO location (name, address, postcode, city, rha)
SELECT DISTINCT initcap(textbox10), textbox17, right(textbox20, 7), 'Saskatoon', textbox23 
FROM importData;


INSERT INTO inspection (location_id, inspection_date, inspection_type, reinspection_priority) 
SELECT DISTINCT id, to_date(textbox35, 'YYYY MM DD'), textbox43, textbox39 
FROM importData, location 
WHERE initcap(textbox10) = location.name AND textbox17 = location.address AND right(textbox20, 7) = location.postcode;



INSERT INTO violation_type (id, description,  priority) 
SELECT DISTINCT CAST(left(textbox16, 2) AS smallint), textbox16, textbox9 FROM importData WHERE textbox16 NOT IN ('');



INSERT INTO violation (inspection_id, violation_id) 
SELECT inspection.id, CAST(left(textbox16, 2) AS smallint) 
FROM importData, location, inspection WHERE textbox16 NOT IN ('') AND initcap(textbox10) = location.name AND textbox17 = location.address AND right(textbox20, 7) = location.postcode AND to_date(textbox35, 'YYYY MM DD') = inspection.inspection_date AND inspection.location_id = location.id;