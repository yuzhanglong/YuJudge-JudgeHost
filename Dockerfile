FROM centos:7
WORKDIR .

COPY  . .

# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo 'Asia/Shanghai' > /etc/timezone

# 安装运行时依赖
RUN yum -y upgrade
RUN yum -y install java-1.8.0-openjdk.x86_64 java-1.8.0-openjdk-devel python3 gcc gcc-c++ libseccomp-devel git cmake make zip unzip
RUN wget https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
RUN tar -zxvf apache-maven-3.5.4-bin.tar.gz
ENV PATH=$PATH:/root/apache-maven-3.5.4/bin/

# 构建产物
RUN chmod 777 ./scripts/build.sh
RUN ./scripts/build.sh

# 暴露8080端口
EXPOSE 8080
ENTRYPOINT ["./dist/entrypoint.sh"]
