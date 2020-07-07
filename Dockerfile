FROM centos:7
WORKDIR .
COPY YuJudge-JudgeHost-0.0.1-SNAPSHOT.jar home/app.jar
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo 'Asia/Shanghai' >/etc/timezone \
    && yum -y upgrade \
    && yum -y install python3 gcc gcc-c++ \
    && mkdir submissions \
    && cd home \

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]