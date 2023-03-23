### ubuntu安装go
1, 下载安装包并解压
```shell
wget https://studygolang.com/dl/golang/go1.20.2.linux-amd64.tar.gz 

tar -xf go1.20.2.linux-amd64.tar.gz -C /usr/local #解压到指定目录
```

2. 设置环境变量
```shell
vim /etc/profile #打开环境变量配置文件

export PATH=$PATH:/usr/local/go/bin #添加执行路径
export GO111MODULE=on #开启gomod
export GOPROXY=https://goproxy.cn #设置国内源
export CGO_ENABLED=1 #开启支持cgo编译


保存修改后执行立即生效
source /etc/profile
```

3. 测试
```shell
go version 
显示 go version go1.20.2 linux/amd64
```
