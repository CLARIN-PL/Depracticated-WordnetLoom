NAME=wordnetloom
mvn clean install && docker build -t clarinpl/${NAME} .
docker kill ${NAME}
docker rm ${NAME}
docker run -p 8080:8080 -p 9990:9990 --net="host" --name ${NAME}  clarinpl/${NAME}