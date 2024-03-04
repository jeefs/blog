### 一,[安装docker](../codeNotes/install-docker.md)

### 二，编写app
```
//创建项目目录
mkdir go-hello-app

cd  ./go-hello-app

//初始化go module
go mod init go-hello-app

vim main.go
//输入测试代码

package main

import (
    "fmt"
    "net/http"
)

func index(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintf(w, "<h1>Hello World</h1>")
}

func check(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintf(w, "<h1>Health check</h1>")
}

func main() {
    http.HandleFunc("/", index)
    http.HandleFunc("/health_check", check)
    fmt.Println("Server starting...")
    http.ListenAndServe(":8080", nil)
}
```

### 三,打包镜像
```
vim Dockerfile

//输入
FROM golang:1.17.2-alpine3.14
MAINTAINER mike@gmail.com
RUN mkdir /app
COPY . /app
WORKDIR /app
RUN go build -o main .
CMD ["/app/main"]

//执行打包命令
docker build -t jeefs:go-hello-app:v0.0.1 .

注意：
1.推送镜像前需要先注册docker hub 账号，https://hub.docker.com/
2.需要给docker设置代理,创建 /etc/systemd/system/docker.service.d/https-proxy.conf文件，输入

[Service]
Environment="HTTPS_PROXY=http://192.168.50.105:7890/"

保存代理后执行
systemctl daemon-reload
systemctl restart docker
systemctl show --property=Environment docker
docker login 登录


//推送镜像到docker hub
docker push jeefs:go-hello-app:v0.0.1
```

### 四，部署镜像到k8s
```
//本地运行测试一下
docker run -d -p 8080:8080 --rm --name go-hello-app-container jeefs/go-hello-app:v0.0.1
```

