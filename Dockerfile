FROM lpicanco/java11-alpine
MAINTAINER zhanyeye zhanyeye@qq.com
WORKDIR /ROOT
ADD ./target/dingtalk-0.0.1-SNAPSHOT.jar /ROOT/
# 缺少字体的问题: https://github.com/AdoptOpenJDK/openjdk-docker/issues/75#issuecomment-445815730
# APK安装软件速度慢: https://www.langxw.com/2021/02/02/%E5%88%B6%E4%BD%9CDockerImage%E5%AD%98%E5%9C%A8%E7%9A%84%E9%97%AE%E9%A2%98/
RUN sed -i "s/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g" /etc/apk/repositories
RUN apk add --update --no-cache ttf-dejavu fontconfig && rm -rf /var/cache/apk/*
ENV LANG en_US.UTF-8
RUN ln -s /lib/libc.musl-x86_64.so.1 /usr/lib/libc.musl-x86_64.so.1
ENV LD_LIBRARY_PATH /usr/lib
ENV PROFILES="prod"
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar dingtalk-0.0.1-SNAPSHOT.jar --spring.profiles.active=$PROFILES"]
