# 构建编译所需要的文件
rm -rf build

mkdir build

cp -r src/main/java/com/yzl/judgehost/scripts build
cp target/YuJudge-JudgeHost-0.0.1-SNAPSHOT.jar build/app.jar
cp Dockerfile build