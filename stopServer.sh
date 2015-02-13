#!/usr/bin/env bash

# Play creates this file when it starts. The file should be removed
# automatically. Note that new instances can't run if the file exists.
kill `cat target/universal/stage/RUNNING_PID`
