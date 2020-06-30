#!/bin/bash

# 保存、编译用户的代码（或者生成运行脚本）
# 你需要传入：1、代码文件（字符串） 2、代码保存的路径
# 脚本将生成 run的脚本/可执行文件

# example: ./compile <本次提交的工作目录> <代码保存目录>  <用户代码> <编译脚本>

SUBMISSION_PATH=$1;
CODE_PATH=$2;
CODE=$3;
BUILDING_SCRIPT=$4;

# 创建提交工作目录
mkdir $SUBMISSION_PATH;

# 创建代码路径，写入用户代码
touch $CODE_PATH;
echo -e "$CODE" >> $CODE_PATH;

BUILDING_SCRIPT_PATH=""$SUBMISSION_PATH"/build.sh"

# 创建编译脚本目录
touch $BUILDING_SCRIPT_PATH;

# cd 到本次提交的工作目录
echo -e "$BUILDING_SCRIPT" >> $BUILDING_SCRIPT_PATH;
chmod 777 $BUILDING_SCRIPT_PATH;

# 执行编译
$BUILDING_SCRIPT_PATH

# 初始化runner
cd $SUBMISSION_PATH
touch run;
chmod 777 ./run;