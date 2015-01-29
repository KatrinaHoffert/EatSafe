 -- For cmpt371 project: EatSafe 
 -- this file insert data from importDate (buffer table)
begin;
    -- drop tables first

INSERT INTO location (locationRestaruant, locationAddress, locationPostcode, locationCity, locationProvince, locationRHA)
SELECT DISTINCT textbox10, textbox17, right(textbox20, 7), 'Saskatoon', 'Saskatchewan', 'Saskatoon Health Authority' FROM importData;


INSERT INTO inspection (inspectionRestaruant, inspectionAddress, inspectionPostcode, inspectionInsDate, inspectionInsType, inspectionInsPriority) 
SELECT DISTINCT textbox10, textbox17, right(textbox20, 7), to_date(textbox35, 'YYYY MM DD'), textbox43, textbox39 FROM importData;

INSERT INTO violation_types (vioViolationId, vioViolationDes,  vioViolationPriority) 
SELECT DISTINCT CAST(left(textbox16, 2) AS smallint), textbox16, textbox9 FROM importData WHERE textbox16 NOT IN ('');

INSERT INTO violations (vioRestaruant, vioAddress, vioPostcode, vioInsDate, vioViolationId) 
SELECT textbox10, textbox17, right(textbox20, 7), to_date(textbox35, 'YYYY MM DD'), CAST(left(textbox16, 2) AS smallint) FROM importData WHERE textbox16 NOT IN ('');