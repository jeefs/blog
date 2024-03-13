### 先决条件
- 已安装jdk17或21 
- 已安装maven

### 创建项目
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
- xxxApplication.java    项目启动文件
- application.properties 项目配置文件
- pom.xml maven包依赖配置文件
### 配置

maven pom.xml配置:
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.3</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.mike.study</groupId>
    <artifactId>spring-boot-demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>spring-boot-demo</name>
    <description>spring-boot-demo</description>
    <properties>
        <java.version>21</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.49</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```


application.yaml配置
```
server:
    port: 8088  #访问端口 
spring:
    application:
        name: spring-boot-demo
    datasource:
        driver-class-name: com.mysql.jdbc.Driver  #数据源驱动
        url: jdbc:mysql://127.0.0.1:3306/test?useSSL=false  #数据连接信息
        username: root  #账户
        password: xxxxxx #密码
```
### 测试
在包目录下创建controller目录，并创建HelloWorld.java控制器文件
```
package com.mike.study.springbootdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  //控制器注解
@RequestMapping("/hello") //路由映射注解
public class HelloWorld {
    @GetMapping("/helloworld") //路由映射注解
    public String helloWorld() {
        return "hello world!";
    }
}

```
路由绑定注解
- @RequestMapping(method = RequestMethod.POST,value = "/path") #指定请求方式，路由,不指定请求方式，则支持任意方式
- @GetMapping(path)   #等价RequestMapping(method = RequestMethod.GET,value = "/path")
- @PostMapping(path)  #等价RequestMapping(method = RequestMethod.POST,value = "/path")

请求参数绑定注解
- @RequestBody  User user #json参数到User对象
- @RequestParam("id") Integer id #url参数到变量
- @ModelAttribute User user #表单参数绑定到User对象
- @PathVariable("id")  Integer id #path参数绑定到变量
- @InitBinder，@WebDataBinder 自定义绑定

请求验证注解
- @Validated User user #请求验证
- @NotEmpty 字段不能为空
- @NotNull  字段不能为null

运行项目后打开浏览器访问:http://localhost:8088/hello/helloworld ，会输出 hello world字符

参考:
- https://blog.csdn.net/albert_xjf/article/details/131576898
- https://blog.csdn.net/xia15000506007/article/details/92799031
- https://maxqiu.com/article/detail/117
