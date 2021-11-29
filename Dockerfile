FROM openjdk:11
COPY target/my_springboot.jar ./my_springboot.jar
COPY src/main/resources/upload_images src/main/resources/upload_images
ENTRYPOINT ["java","-jar","./my_springboot.jar"]