
<p align="center">
  <a href="">
    <img src="http://cdn.yuzzl.top/judge.png">
  </a>
</p>

<p align="center">
  
</p>

### 文档地址

[]()

### 在线实例


### 版本日志

最新版本 `1.0.0`

#### 1.0.0

发布前后端项目至GitHub

---
### YuJudge-JudgeHost

这是YuJudge项目的**判题服务器**，基于java的springboot框架。

## 项目结构


```
YuJudge-JudgeHost                                          
├─ .gitignore                                                      
├─ Dockerfile                                                      // dockerfile配置
├─ README.md                                                       // 项目自述文件
├─ build.sh                                                        // 安装脚本
├─ help                                                            
│  └─ work.png                                                     
├─ mvnw                                                            
├─ mvnw.cmd                                                        
├─ pom.xml                                                         // pom配置
└─ src                                                             
   ├─ main                                                         
   │  ├─ java                                                      
   │  │  └─ com                                                    
   │  │     └─ yzl                                                 
   │  │        └─ judgehost                                        
   │  │           ├─ JudgeHostApplication.java                     // 应用入口
   │  │           ├─ api                                           // Controller层
   │  │           │  └─ v1                                         
   │  │           │     ├─ AuthorizationController.java            // 身份验证相关接口
   │  │           │     ├─ FileController.java                     // 文件处理相关接口
   │  │           │     └─ JudgeController.java                    // 判题接口
   │  │           ├─ core                                          // 项目核心
   │  │           │  ├─ authorization                              // 权限管理相关
   │  │           │  │  ├─ AuthorizationInterceptor.java           
   │  │           │  │  └─ AuthorizationRequired.java              
   │  │           │  ├─ common                                     // 一般内容
   │  │           │  │  ├─ DocumentResponse.java                   
   │  │           │  │  ├─ JudgeThreadFactory.java                 // 判题线程池工厂
   │  │           │  │  └─ UnifiedResponse.java                    // http统一返回
   │  │           │  ├─ configuration                              // 相关配置
   │  │           │  │  ├─ AuthorizationConfiguration.java         // 权限配置
   │  │           │  │  ├─ ExceptionCodeConfiguration.java         // 异常配置
   │  │           │  │  ├─ JudgeEnvironmentConfiguration.java      // 判题环境配置
   │  │           │  │  └─ JudgeExecutorConfiguration.java         // 线程池配置
   │  │           │  ├─ enumerations                               // 枚举类
   │  │           │  │  ├─ JudgeConfigDefaultEnum.java             // 判题配置
   │  │           │  │  ├─ JudgePreferenceEnum.java                // 判题偏好
   │  │           │  │  ├─ JudgeResultEnum.java                    // 判题结果
   │  │           │  │  └─ LanguageScriptEnum.java                 // 判题语言
   │  │           │  └─ handlers                                   
   │  │           │     ├─ GlobalExceptionHandler.java             // 全局异常处理
   │  │           │     └─ HttpRequestHandler.java                 // http请求处理
   │  │           ├─ dto                                           // 数据传输对象
   │  │           │  ├─ AccessTokenDTO.java                        
   │  │           │  ├─ AuthorizationDTO.java                      
   │  │           │  ├─ JudgeDTO.java                              
   │  │           │  ├─ ResolutionDTO.java                         
   │  │           │  └─ SingleJudgeResultDTO.java                  
   │  │           ├─ exception                                     // 异常相关
   │  │           │  └─ http                                       // http异常相关
   │  │           │     ├─ ForbiddenException.java                 
   │  │           │     ├─ HttpException.java                      
   │  │           │     └─ NotFoundException.java                  
   │  │           ├─ network                                       // 网络请求相关
   │  │           │  └─ HttpRequest.java                           
   │  │           ├─ scripts                                       // 依赖的脚本
   │  │           │  ├─ compare.sh                                 
   │  │           │  ├─ compile.sh                                 
   │  │           ├─ service                                       // Service层
   │  │           │  ├─ AuthorizationService.java                
   │  │           │  ├─ FileService.java                          
   │  │           │  └─ JudgeService.java                        
   │  │           ├─ utils                                         // 工具类
   │  │           │  ├─ DataReformat.java                          
   │  │           │  ├─ FileHelper.java                           
   │  │           │  ├─ TokenHelper.java                         
   │  │           │  └─ YamlPropertySourceFactory.java            
   │  │           ├─ validators                                    // 验证器
   │  │           │  ├─ LanguageTypeAccepted.java                
   │  │           │  ├─ LanguageTypeAcceptedValidator.java        
   │  │           │  ├─ LoginValidated.java                       
   │  │           │  └─ LoginValidator.java                      
   │  │           └─ vo                                            // 表现层对象
   │  │              ├─ AuthorizationVO.java                       
   │  │              └─ JudgeConditionVO.java                      
   │  └─ resources                                                 // 项目配置文件
   │     ├─ application.yml                                        // 总配置
   │     └─ config                                                 
   │        ├─ application-dev.yml                                 // 开发环境配置
   │        ├─ application-prod.yml                                // 生产环境配置
   │        ├─ exception-codes.yml                                 // 错误码配置
   │        └─ judge-environment.yml                               // 判题环境配置
   └─ test                                                         // 测试相关，包含jmeter的配置文件
```
