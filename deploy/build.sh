#!/bin/sh

# 构建编译所需要的文件

cd ..

rm -rf build

mkdir build

cp -r src/main/java/com/yzl/judgehost/scripts build
cp target/YuJudge-JudgeHost-1.0.jar build/app.jar
cp deploy/Dockerfile build
cp deploy/entrypoint.sh build