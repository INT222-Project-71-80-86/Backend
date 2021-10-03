FROM maven:3.6.0-jdk-11 AS build
COPY . ./
RUN mvn clean package -DskipTests

FROM openjdk:11
COPY --from=build target/*.jar ./app.jar
COPY src/main/resources/upload_images src/main/resources/upload_images
ENTRYPOINT ["java","-jar","./app.jar"]