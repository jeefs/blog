### 先决条件
- 已安装jdk17或21 
- 已安装maven

### 创建项目
1.使用IDEA创建工程
打开IDEA->选择File->New-Project->选择Spring Initializr，填写项目信息:
- Name ：工程名称
- Location: 工程保存路径
- Language: 选择JVM语言，一般默认Java
- Type: 项目包管理，选择Maven
- Group: 包组织名称，填写com.mike.study
- Artifact: 包唯一标识,与工程名称相同即可
- Package Name：包名称，非必填，会自动生成
- JDK: 选择JDK版本，选择JDK17或JDK21
- Java：项目java版本,与JDK版本保持一致
- Packaging: 打包方式,选择Jar

填写完毕后点击Next，进入选择Spring Boot版本及依赖项：
- Spring Boot版本选最新的稳定版本(非SNAPSHOT即可)
- Web依赖选择Spring Web

点击Create创建项目，会生成项目结构:
- xxxApplication.java 文件为项目启动文件
- application.properties 文件为项目配置文件
- pom.xml 文件为maven包依赖配置文件

### 测试
//配置访问端口
打开application.properties文件，添加
```
server.port=8088
```

//编写控制器
在包目录下创建controller目录，并创建HelloWorld.java控制器文件
```
package com.mike.study.springbootdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  //控制器注解
@RequestMapping("/hello") //路由映射注解
public class HelloWorld {
    @RequestMapping("/helloworld") //路由映射注解
    public String helloWorld() {
        return "hello world!";
    }
}

```

在Application.java文件上运行项目后打开浏览器访问:http://localhost:8088/hello/helloworld ，会输出 hello world字符
