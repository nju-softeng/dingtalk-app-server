#FROM openjdk:11
#MAINTAINER LiXiaoKang 191250075@smail.nju.edu.cn
#WORKDIR /ROOT
#ADD ./target/dingtalk-0.0.1-SNAPSHOT.jar /ROOT/
#ENV LD_LIBRARY_PATH /usr/lib
#ENV PROFILES="prod"
#EXPOSE 8080
#ENTRYPOINT ["sh", "-c", "java -jar dingtalk-0.0.1-SNAPSHOT.jar --spring.profiles.active=$PROFILES"]

# First stage: complete build environment
FROM maven:3.8.6-jdk-11-slim AS builder
WORKDIR /code
COPY . /code
# add pom.xml and source code
ADD ./pom.xml /code/pom.xml
ADD ./src /code/src
# package jar
COPY ./settings.xml /root/.m2/settings.xml
RUN mvn package -DskipTests

# Second stage: minimal runtime environment
FROM openjdk:11
WORKDIR /app
# Change TimeZone
# RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
LABEL maintainer="191250075@smail.nju.edu.cn"
# copy jar from the first stage
COPY --from=builder /code/target/dingtalk-2.0.1-SNAPSHOT.jar /app/app.jar

ENV LD_LIBRARY_PATH /usr/lib
ENV PROFILES="prod"
EXPOSE 8080
# run jar
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --spring.profiles.active=$PROFILES"]
