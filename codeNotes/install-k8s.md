###  安装kubectl
1.下载最新安装包

```shell
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
```

2.校验文件完整性

- 下载 kubectl 校验和文件：

```shell
curl -LO "https://dl.k8s.io/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl.sha256"  
```

- 验证 kubectl 的可执行文件

```shell
echo "$(cat kubectl.sha256)  kubectl" | sha256sum --check
```

- 校验成功输出如下

```shell
kubectl: OK
```

3.安装kubectl

```shell
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

4.测试，打印版本信息

```shell
kubectl version --client --output=yaml
```

- 会输出如下信息

```
clientVersion:
  buildDate: "2023-01-18T15:58:16Z"
  compiler: gc
  gitCommit: 8f94681cd294aa8cfd3407b8191f6c70214973a4
  gitTreeState: clean
  gitVersion: v1.26.1
  goVersion: go1.19.5
  major: "1"
  minor: "26"
  platform: linux/amd64
kustomizeVersion: v4.5.7

```

###  安装本地集群
- [minikube](https://github.com/kubernetes/minikube)
- [kind](https://github.com/kubernetes-sigs/kind)

1.安装kind

```
curl -Lo ./kind "https://kind.sigs.k8s.io/dl/v0.17.0/kind-$(uname)-amd64"
chmod +x ./kind
sudo mv ./kind /usr/local/bin/kind
```

2.创建cluster
```
kind create cluster
```

查看创建成功的节点

```
kubectl get nodes
```

3.删除cluster

```
kind delete cluster
```