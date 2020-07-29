package com.yzl.judgehost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yuzhanglong
 * @email yuzl1123@163.com
 * @version demo-test
 *
 * 这是我的OnlineJudge项目--YuJudge的JudgeHost部分（专门判题的服务器）
 * https://github.com/yuzhanglong/YuJudge-JudgeHost
 * 基于Java的springboot框架
 *
 * 本判题服务器（JudgeHost）负责接收用户的提交 并将代码编译、运行、比较，并返回判断情况
 * 其中，代码运行的核心被单独分离在这个仓库 https://github.com/yuzhanglong/YuJudge-Core
 *
 * 考虑到判题的速度、短时间内可能需要大量判题，JudgeHost可能需要考虑多线程、集群相关
 */

@SpringBootApplication
public class JudgeHostApplication {
    public static void main(String[] args) {
        SpringApplication.run(JudgeHostApplication.class, args);
    }
}
