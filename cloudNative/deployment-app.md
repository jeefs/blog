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

//创建部署配置文件
vim deployment.yaml
输入
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: go-hello-app
  namespace: my-workspace # 声明工作空间，默认为default
spec:
  replicas: 2
  selector:
    matchLabels:
      name: go-hello-app
  template:
    metadata:
      labels:
        name: go-hello-app
    spec:
      containers:
        - name: go-hello-container
          image: jeefs/go-hello-app:v0.0.1
          imagePullPolicy: IfNotPresent
          ports:
            - containerPort: 8080 # containerPort是声明容器内部的port

---
apiVersion: v1
kind: Service
metadata:
  name: go-hello-app-service
  namespace: my-workspace # 声明工作空间，默认为default
spec:
  type: NodePort
  ports:
    - name: http
      port: 18080 # Service暴露在cluster-ip上的端口，通过<cluster-ip>:port访问服务,通过此端口集群内的服务可以相互访问
      targetPort: 8080 # Pod的外部访问端口，port和nodePort的数据通过这个端口进入到Pod内部，Pod里面的containers的端口映射到这个端口，提供服务
      nodePort: 31080 # Node节点的端口，<nodeIP>:nodePort 是提供给集群外部客户访问service的入口
  selector:
    name: go-hello-app


//执行部署
kubectl create workspace my-workspace  //创建空间
kubectl create -f deployment.yaml  //部署到k8s集群
kubectl get po -n my-workspace //查看部署状态
```
部署成功后可通过 http://nodeIP:nodePort访问app
