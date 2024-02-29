### 官方安装教程：https://github.com/containerd/containerd/blob/main/docs/getting-started.md

### 1、下载安装containerd
```
wget https://github.com/containerd/containerd/releases/download/v1.6.19/containerd-1.6.19-linux-amd64.tar.gz
tar Cxzvf /usr/local containerd-1.6.19-linux-amd64.tar.gz
```

### 2、配置通过systemd启动containd
```
wget https://raw.githubusercontent.com/containerd/containerd/main/containerd.service -o /usr/lib/systemd/system/containerd.service
systemctl daemon-reload && systemctl enable containerd
```

### 3、安装 runc（版本说明：https://github.com/containerd/containerd/blob/release/1.6/script/setup/runc-version）
```
wget https://github.com/opencontainers/runc/releases/download/v1.1.4/runc.amd64
install -m 755 runc.amd64 /usr/local/sbin/runc
```

### 4、安装 CNI plugins（版本说明：https://github.com/containerd/containerd/blob/release/1.6/go.mod）
```
wget https://github.com/containernetworking/plugins/releases/download/v1.2.0/cni-plugins-linux-amd64-v1.2.0.tgz
mkdir -p /opt/cni/bin
tar Cxzvf /opt/cni/bin cni-plugins-linux-amd64-v1.2.0.tgz
```

### 5、生成 containerd 默认配置文件
```
mkdir /etc/containerd
containerd config default > /etc/containerd/config.toml
```

### 6、修改配置文件，配置 runc 使用 systemd cgroup 驱动，将 SystemdCgroup 改为 true 注意配置项是区分大小写的
### 并将 sandbox_image 地址修改为国内的地址，配置镜像加速器
### 文档：https://v1-26.docs.kubernetes.io/zh-cn/docs/setup/production-environment/container-runtimes/#containerd
### 文档：https://github.com/containerd/cri/blob/master/docs/registry.md
```
vi /etc/containerd/config.toml
[plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc]
  ...
  [plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc.options]
    SystemdCgroup = true

并且将 sandbox_image = "registry.k8s.io/pause:3.6"
修改为 sandbox_image = "registry.cn-hangzhou.aliyuncs.com/google_containers/pause:3.6"

### 配置镜像加速器，在 [plugins."io.containerd.grpc.v1.cri".registry.mirrors] 后面增加两行配置，注意缩进
[plugins."io.containerd.grpc.v1.cri".registry.mirrors]
  [plugins."io.containerd.grpc.v1.cri".registry.mirrors."docker.io"]
    endpoint = ["http://mirrors.ustc.edu.cn"]
  [plugins."io.containerd.grpc.v1.cri".registry.mirrors."*"]
    endpoint = ["http://hub-mirror.c.163.com"]
```
### 7、启动 containerd
```
systemctl restart containerd
```
### 启动成功后可以查看到监听的端口
```
netstat -nlput | grep containerd
tcp        0      0 127.0.0.1:36669         0.0.0.0:*               LISTEN      8665/containerd      off (0.00/0/0)
```
