#!/bin/bash

mvn clean install

rmdir -p client/lib
mkdir -p client/lib
cp ./wordnetloom-client-updater/target/updater/updater.jar client/lib
cp ./wordnetloom-client/start.sh client/
cp ./wordnetloom-client/start.bat client/