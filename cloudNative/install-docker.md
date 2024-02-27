### 系统版本
ubuntu 18.04 LTS

### 安装
```
sudo apt update  //更新包

sudo apt install apt-transport-https ca-certificates curl software-properties-common  //安装依赖包

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add - //添加仓库密钥

sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"  //添加源地址

sudo apt update //更新包

apt-cache policy docker-ce  //获取当前包安装状态

sudo apt install docker-ce  //安装docker

sudo systemctl status docker //查看docker运行状态
```
