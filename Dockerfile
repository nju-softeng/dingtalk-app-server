FROM lpicanco/java11-alpine
MAINTAINER zhanyeye zhanyeye@qq.com
WORKDIR /ROOT
ADD ./target/dingtalk-0.0.1-SNAPSHOT.jar /ROOT/
ENTRYPOINT ["java", "-jar", "dingtalk-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
CMD ["java", "-version"]
