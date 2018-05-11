set WORDNETLOOM_SERVER_HOST=localhost
set WORDNETLOOM_SERVER_PORT=8081
java -jar lib/updater.jar
java -jar lib/wordnetloom-client.jar > errors.log
