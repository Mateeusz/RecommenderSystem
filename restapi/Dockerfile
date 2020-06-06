FROM alpine

RUN apk add --no-cache openjdk8
RUN apk add --no-cache git
RUN apk add --no-cache maven
#git cloning
RUN git clone https://github.com/Mateeusz/RecommenderSystem.git

#Set working directory
WORKDIR /RecommenderSystem/restapi

# install dependencies
RUN mvn clean package

#RUN mvn clean install

CMD ["java", "-jar", "./target/restapi-0.0.1-SNAPSHOT.jar"]

# tell the port number the container should expose
EXPOSE 8080