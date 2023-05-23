FROM centos:7
WORKDIR .

COPY  . .

# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo 'Asia/Shanghai' > /etc/timezone

# 安装依赖
RUN yum -y upgrade \
    && yum -y install java-1.8.0-openjdk.x86_64 java-1.8.0-openjdk-devel python3 gcc gcc-c++ libseccomp-devel git cmake make zip unzip

# 构建产物
RUN chmod 777 ./scripts/build.sh
RUN ./scripts/build.sh

# 暴露8080端口
EXPOSE 8080
ENTRYPOINT ["./dist/entrypoint.sh"]
