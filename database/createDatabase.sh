#!/usr/bin/env bash

# Creates the database. Will destroy and recreate if the database already exists. It's recommended
# to use this instead of running the scripts individually to avoid missing a step.
#
# If you're trying to update the database, you need to run `backupCoordinates.sh` first. It'll
# backup the coordinates table, which are restored by this script.
#
# Note: eatsafe_synonyms.syn MUST be placed in the /usr/share/postgresql/9.3/tsearch_data
# directory before this script is run. Note the "9.3" in the path is the PostgreSQL version and
# should be changed to wwhatever version you're running.

#################
# CONFIGURATION #
#################
# In order to run this script, you must have a database setup and provide the credentials below.
# The database can be empty or in-use, although it's recommended to use a new database to avoid
# conflicts.
DBHOST=cmpt371g1.usask.ca
DBNAME=eatsafe
USERNAME=eatsafe
PASSWORD=M4PGx3zUySvMmB9j
# End of configuration

export PGPASSWORD="$PASSWORD"

FILES_TO_RUN=(
  "createTables.sql"
  "statements.sql"
  "restoreCoordinates.sql"
  "createSearchTable.sql"
)

# Contenates the files with line breaks between <http://stackoverflow.com/a/8183247/1968462>
awk 'FNR==1{print ""}1' ${FILES_TO_RUN[*]} > tmp.sql
psql --host="$DBHOST" --dbname="$DBNAME" --username="$USERNAME" -1 -f tmp.sql

# On success, remove the temporary file. Keep it if there was an error, so that the line numbers
# in the error messages can be used
if [ $? -eq 0 ]; then
  rm tmp.sql
fi

# Correct sequences is special
psql --host="$DBHOST" --dbname="$DBNAME" --username="$USERNAME" -Atq -f "correctSequences.sql" -o tmp_seq_correct
psql --host="$DBHOST" --dbname="$DBNAME" --username="$USERNAME" -f tmp_seq_correct
rm tmp_seq_correct