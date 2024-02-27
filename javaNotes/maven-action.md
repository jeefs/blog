### 1.下载安装包
[下载地址直达](https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip)
下载完成后，解压到本地目录


### 2.配置环境变量
打开，我的电脑-》高级系统设置-》高级-》环境变量-》系统变量-》新建:
```
变量名:MAVEN_HOME 
变量值:D:\Program Files\apache-maven-3.9.6   //需要填写具体的解压目录
```
在系统变量-》Path下面新增一条记录:
```
%MAVEN_HOME%\bin
```
设置完成后确定保存

### 3.验证安装正确性
打开cmd控制台输入
```
mvn -version
```

输出如下命令则安装成功
```
Apache Maven 3.9.6 (bc0240f3c744dd6b6ec2920b3cd08dcc295161ae)
Maven home: D:\Program Files\apache-maven-3.9.6
Java version: 1.8.0_401, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-1.8\jre
Default locale: zh_CN, platform encoding: GBK
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"

```

### 4.IDEA配置阿里云镜像及本地仓库
1.打开IDEA安装目录下的D:\Program Files\JetBrains\IntelliJ IDEA 2023.3.4\plugins\maven\lib\maven3\conf\settings.xml
在<mirrors></mirrors>中追加
```
    <mirror>
      <id>aliyunmaven</id>
      <mirrorOf>*</mirrorOf>
      <name>阿里云公共仓库</name>
      <url>https://maven.aliyun.com/repository/public</url>
    </mirror>

```

配置本地仓库路径:
```
  <localRepository>/path/to/local/repo</localRepository>
```

### 5.依赖配置
maven结构
```
├── pom.xml //项目描述文件
├── src
│   ├── main
│   │   ├── java //源码目录
│   │   └── resources  //资源文件目录
│   └── test
│       ├── java  //测试源码目录
│       └── resources  //测试资源文件
└── target //打包生成文件目录
```

描述文件内容
```
<project ...>
	<modelVersion>4.0.0</modelVersion>  //描述文件版本号
	<groupId>com.mike.learnjava</groupId> //项目组织名称，一般对对应包名
	<artifactId>base-java</artifactId> //项目唯一标识
	<version>1.0</version> //项目特定版本号
	<packaging>jar</packaging> //打包后输出格式，默认是.jar
	<properties>
        ...
	</properties>
	<dependencies>
        <dependency>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
            <version>1.2</version>
        </dependency>
	</dependencies>
</project>
```

###   
