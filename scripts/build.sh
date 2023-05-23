# 构建项目

# 初始化构建产物目录
rm -rf dist
mkdir dist
mkdir dist/scripts

# 保存运行时脚本
cp ./scripts/compile.sh dist/scripts/compile.sh || exit
cp ./scripts/compare.sh dist/scripts/compare.sh || exit
cp ./scripts/entrypoint.sh dist/scripts/entrypoint.sh || exit

# 安装依赖、构建 Java 项目
mvn package
cp target/YuJudge-JudgeHost-1.0.jar dist/app.jar

# 拉取、编译、保存判题核心可执行文件
rm -rf YuJudge-Core
git clone https://github.com/yuzhanglong/YuJudge-Core.git --depth=1
cd YuJudge-Core || exit
chmod 777 ./build.sh && ./build.sh
cp build/y_judge /dist/scripts/y_judge || exit
rm -rf YuJudge-Core

# 初始化临时目录，并将相关文件移动到临时目录
mkdir -p /home/judgeEnvironment/resolutions
mkdir -p /home/judgeEnvironment/submissions
mkdir -p /home/judgeEnvironment/scripts
cp -r ./dist/scripts/* /home/judgeEnvironment/scripts
chmod 777 -R /home/judgeEnvironment/scripts
