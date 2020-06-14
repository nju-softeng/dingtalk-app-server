FROM lpicanco/java11-alpine
MAINTAINER zhanyeye zhanyeye@qq.com
WORKDIR /ROOT
ADD ./target/dingtalk-0.0.1-SNAPSHOT.jar /ROOT/
ENV PROFILES="prod"
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar dingtalk-0.0.1-SNAPSHOT.jar --spring.profiles.active=$PROFILES"]