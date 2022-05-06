FROM openjdk:11
MAINTAINER zhanyeye zhanyeye@qq.com
WORKDIR /ROOT
ADD ./target/dingtalk-0.0.1-SNAPSHOT.jar /ROOT/
ENV LD_LIBRARY_PATH /usr/lib
ENV PROFILES="prod"
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar dingtalk-0.0.1-SNAPSHOT.jar --spring.profiles.active=$PROFILES"]