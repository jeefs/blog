## ubuntu server 20.04 lts 安装docker

1.cat /proc/version 查看ubuntu版本，添加对应国内源
第一步：备份源文件：
```shell
sudo cp /etc/apt/sources.list /etc/apt/sources.list.backup
```

第二步：编辑/etc/apt/sources.list文件，vi /etc/apt/sources.list，写入
```shell
deb http://mirrors.163.com/ubuntu/ focal main restricted universe multiverse
deb http://mirrors.163.com/ubuntu/ focal-security main restricted universe multiverse
deb http://mirrors.163.com/ubuntu/ focal-updates main restricted universe multiverse
deb http://mirrors.163.com/ubuntu/ focal-backports main restricted universe multiverse
# deb-src http://mirrors.163.com/ubuntu/ focal main restricted universe multiverse
# deb-src http://mirrors.163.com/ubuntu/ focal-security main restricted universe multiverse
# deb-src http://mirrors.163.com/ubuntu/ focal-updates main restricted universe multiverse
# deb-src http://mirrors.163.com/ubuntu/ focal-backports main restricted universe multiverse
```

2.更新源
```shell
sudo apt update
sudo apt upgrade
sudo apt full-upgrade
```

3.安装必要的证书并允许 apt 包管理器使用以下命令通过 HTTPS 使用存储库
```shell
sudo apt install apt-transport-https ca-certificates curl software-properties-common gnupg lsb-release
```

4.添加 Docker 的官方 GPG 密钥
```shell
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
```

5.添加 Docker 官方库：
```shell
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

6.更新源列表
```shell
sudo apt update
```
 
7.安装最新 Docker CE
```shell
sudo apt install docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

apt-cache madison docker-ce 命令可以检查当前支持的其他版本docker
输出
docker-ce | 5:20.10.17~3-0~ubuntu-jammy | https://download.docker.com/linux/ubuntu jammy/stable amd64 Packages
docker-ce | 5:20.10.16~3-0~ubuntu-jammy | https://download.docker.com/linux/ubuntu jammy/stable amd64 Packages
docker-ce | 5:20.10.15~3-0~ubuntu-jammy | https://download.docker.com/linux/ubuntu jammy/stable amd64 Packages
docker-ce | 5:20.10.14~3-0~ubuntu-jammy | https://download.docker.com/linux/ubuntu jammy/stable amd64 Packages
docker-ce | 5:20.10.13~3-0~ubuntu-jammy | https://download.docker.com/linux/ubuntu jammy/stable amd64 Packages

安装 5:20.10.16~ 3-0 ~ubuntu-jammy 这个版本
sudo apt install docker-ce=5:20.10.16~3-0~ubuntu-jammy docker-ce-cli=5:20.10.16~3-0~ubuntu-jammy containerd.i

8.验证 Docker 服务是否在运行
```shell
systemctl status docker
```

如果没有启动，执行sudo systemctl start docker，启动

9.加入开机启动
```shell
sudo systemctl enable docker
```

10.查看docker版本
```shell
sudo docker version
```
 
