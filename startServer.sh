#!/usr/bin/env bash

# Note that we cannot start on a port lower than 1024 without root.
# So I configured port 80 to redirect to 8080 so that this script
# can be run as non-root.

# Must stop the server if it's already running, otherwise starting
# again will fail. But we can only do this if the RUNNING_PID file
# exists (which it should if the server is running).
if [ -e "target/universal/stage/RUNNING_PID" ]
  then
    echo "Stopping existing server."
    ./stopServer.sh
fi

echo "Starting new server."
nohup ./activator start -Dhttp.port=8080 -Dsbt.log.noformat=true  > nohup.out \
    2> errors.log < /dev/null &
