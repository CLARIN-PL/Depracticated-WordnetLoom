NAME=wordnetloom
mvn clean install && docker build -t clarinpl/${NAME} .
docker kill ${NAME}
docker rm ${NAME}
docker run -d -p 8080:8081 -p 9990:9991 --net="host" --name ${NAME}  clarinpl/${NAME}

rm -r client
mkdir -p client/lib
cp ./wordnetloom-client-updater/target/updater/updater.jar client/lib
cp ./wordnetloom-client/start.sh client/
cp ./wordnetloom-client/start.bat client/
