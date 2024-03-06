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

### 卸载方法
```
1.删除docker及安装时自动安装的所有包
apt-get autoremove docker docker-ce docker-engine  docker.io  containerd runc

2.查看docker是否卸载干净
dpkg -l | grep docker

dpkg -l |grep ^rc|awk '{print $2}' |sudo xargs dpkg -P # 删除无用的相关的配置文件

3.删除没有删除的相关插件
apt-get autoremove docker-ce-*

4.删除docker的相关配置&目录
rm -rf /etc/systemd/system/docker.service.d
rm -rf /var/lib/docker

5.确定docker卸载完毕
docker --version
```
