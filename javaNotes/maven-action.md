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
### 4.使用方法

