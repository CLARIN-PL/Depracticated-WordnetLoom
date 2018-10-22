#!/bin/bash
export WORDNETLOOM_SERVER_HOST=localhost
export WORDNETLOOM_SERVER_PORT=8081
java -jar lib/updater.jar
java -jar lib/wordnetloom-client.jar > error.log