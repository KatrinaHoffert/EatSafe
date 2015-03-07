 -- EatSafe 

 -- For "Default" detabase: When update/re-create the database,
 -- Step 1: run this file to bakcup the coordinate table to a file.
\copy coordinate TO 'coordinateBackup.txt';


 -- Step 2: run "CreateTables.sql" to drop and re-create the schema
 -- Step 3: run "RestoreCoordinates.sql" to restore the coordinate table 
 -- step 4: run "statements.sql" that contains locations, etc
 -- step 5: run the PopulateCoordinate program to update coordinates for newly
 --         added locations


 -- For "test" database: 
 -- Only need to run "CreateTables.sql" to re-create the schema