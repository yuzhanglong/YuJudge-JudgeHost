#!/bin/sh

# 初始化临时目录，并将相关文件移动到临时目录
mkdir -p /home/judgeEnvironment/resolutions
mkdir -p /home/judgeEnvironment/submissions
mkdir -p /home/judgeEnvironment/scripts
cp -r ./scripts/* /home/judgeEnvironment/scripts
chmod 777 -R /home/judgeEnvironment/scripts

# 启动服务器
java -jar app.jar \
          --spring.redis.port=$REDIS_PORT \
          --spring.redis.host=$REDIS_HOST \
          --judge-authorization.user-id=$JUDGE_HOST_USER_ID \
          --judge-authorization.user-secret=$JUDGE_HOST_USER_SECRET \
