### 1.下载安装包
[下载地址直达](https://download.oracle.com/otn/java/jdk/8u401-b10/4d245f941845490c91360409ecffb3b4/jdk-8u401-windows-x64.exe)
下载完成后，安装即可


### 2.配置环境变量
打开，我的电脑-》高级系统设置-》高级-》环境变量-》系统变量-》新建:
```
变量名:JAVA_HOME
变量值: C:\Program Files\Java\jdk-1.8    //需要填写具体的安装目录
```
在系统变量-》Path下面新增一条记录:
```
%JAVA_HOME%\bin
```
设置完成后确定保存

### 3.验证安装正确性
打开cmd控制台输入
```
java -version
输出如下命令则安装成功
java version "1.8.0_401"
Java(TM) SE Runtime Environment (build 1.8.0_401-b10)
Java HotSpot(TM) 64-Bit Server VM (build 25.401-b10, mixed mode)
```

