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

