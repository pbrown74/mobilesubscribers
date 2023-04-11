FROM openjdk:17-oracle
EXPOSE 8080
ADD target/mobile-subscribers.jar mobile-subscribers.jar
ENTRYPOINT ["java","-jar","/mobile-subscribers.jar"]
