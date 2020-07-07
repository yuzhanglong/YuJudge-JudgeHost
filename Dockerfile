FROM centos:7
WORKDIR .


COPY . .

# 安装运行依赖 以及运行环境的初始化
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo 'Asia/Shanghai' > /etc/timezone \
    && mkdir home/judgeEnvironment \
    && cp -r scripts home/judgeEnvironment \
    && mkdir home/judgeEnvironment/resolutions \
    && mkdir home/judgeEnvironment/submissions \
    && yum -y upgrade \
    && yum -y install java-1.8.0-openjdk.x86_64 python3 gcc gcc-c++ libseccomp \
    && chmod 777 -R home/judgeEnvironment/scripts

# 暴露8080端口
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]