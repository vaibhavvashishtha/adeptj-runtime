#!/usr/bin/env bash

###############################################################################
#                                                                             #
#    Copyright 2016, AdeptJ (http://www.adeptj.com)                           #
#                                                                             #
#    Licensed under the Apache License, Version 2.0 (the "License");          #
#    you may not use this file except in compliance with the License.         #
#    You may obtain a copy of the License at                                  #
#                                                                             #
#        http://www.apache.org/licenses/LICENSE-2.0                           #
#                                                                             #
#    Unless required by applicable law or agreed to in writing, software      #
#    distributed under the License is distributed on an "AS IS" BASIS,        #
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. #
#    See the License for the specific language governing permissions and      #
#    limitations under the License.                                           #
#                                                                             #
###############################################################################

# This script is used to stop running instance of adeptj runtime.
# This will find the process and kill it and execute stop command for adeptj.
JAR_NAME="adeptj-runtime.jar"

# preparing base paths
BIN_PATH=$(cd $(dirname "$0") && pwd)
BASE=$(dirname "$BIN_PATH")

# target directory
TARGET=$BASE"/target"

# Moving forward if target directory exist.
if [[ -d "$TARGET" ]]
then
   JAR_PATH=$TARGET"/"$JAR_NAME
   PID_FILE=$BASE"/runtime.pid"

   if [ -e "$PID_FILE" ]
   then
       PID=$(cat "$PID_FILE")
       cd "$BASE"
       # sending stop command to adeptj
       java -jar "$JAR_PATH" stop &

       # killing running adeptj process
       kill "$PID"

       # clean up afterwards
       rm -f "$PID_FILE"
   fi
fi