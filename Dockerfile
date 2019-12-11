FROM lpicanco/java11-alpine
VOLUME /tmp
ADD target/dingtalk-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

