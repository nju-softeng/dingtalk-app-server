FROM lpicanco/java11-alpine
MAINTAINER zhanyeye zhanyeye@qq.com
WORKDIR /ROOT
ADD ./target/dingtalk-0.0.1-SNAPSHOT.jar /ROOT/
# 解决缺少字体的问题: https://github.com/AdoptOpenJDK/openjdk-docker/issues/75#issuecomment-445815730
RUN apk add --update ttf-dejavu fontconfig && rm -rf /var/cache/apk/*
ENV LANG en_US.UTF-8
RUN ln -s /lib/libc.musl-x86_64.so.1 /usr/lib/libc.musl-x86_64.so.1
ENV LD_LIBRARY_PATH /usr/lib
ENV PROFILES="prod"
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar dingtalk-0.0.1-SNAPSHOT.jar --spring.profiles.active=$PROFILES"]