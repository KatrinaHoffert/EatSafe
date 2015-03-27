-- Backs up the coordinate table from the coordinateBackup file.
--
-- Note: in order to ensure that the `createDatabase.sh` script can find the file, you must run
-- this in the database folder.
\copy coordinate TO 'coordinateBackup.txt';