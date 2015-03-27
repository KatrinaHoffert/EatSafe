#!/usr/bin/env bash

# This script backs up the coordinates from the coordinates table in the database into a local file
# which will be restored by the `createDatabase.sh` script (which also re-creates the database, and
# thus the need for this script). If you've updated the coordinates by running the coordinate
# getter auxillary program, then you should run this script prior to running createDatabase.sh
# (if the coordinates haven't been changed, you don't need to run this script).

#################
# CONFIGURATION #
#################
DBHOST=cmpt371g1.usask.ca
DBNAME=eatsafe
USERNAME=eatsafe
PASSWORD=M4PGx3zUySvMmB9j
# End of configuration

export PGPASSWORD="$PASSWORD"

psql --host="$DBHOST" --dbname="$DBNAME" --username="$USERNAME" -f "sql/backupCoordinates.sql"