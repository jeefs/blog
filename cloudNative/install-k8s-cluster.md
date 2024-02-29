### 系统版本
ubuntu 18.04 LTS,采用VMware Workstation创建3台虚拟机，1台master,2台node节点，先配置好master再利用虚拟机快照克隆来复制出2台node虚拟机

### 虚拟机环境配置
1.硬件参数:
- 内存4GB
- cpu 2P2C
- 网络模式 NAT，设置静态IP

2.配置网络:
```
//设置主机名称为master
hostnamectl set-hostname master

//检查是否设置成功
hostnamectl

//设置静态IP，DNS，网关
vim /etc/netplan/00-installer-config.yaml

添加:
# This is the network config written by 'subiquity'
network:
  ethernets:
    ens33:
      #静态ip地址
      addresses: [192.168.123.131/24]
      #网关地址
      gateway4: 192.168.123.2
      #dsn服务器地址
      nameservers:
        addresses: [192.168.123.2,8.8.8.8]
  version: 2

 保存后，执行netplan apply生效
```

3.配置hosts:
```
vim /etc/hosts

添加:
127.0.0.1 localhost
192.168.123.131 master
192.168.123.132 node1
192.168.123.133 node2

# The following lines are desirable for IPv6 capable hosts
::1     ip6-localhost ip6-loopback
fe00::0 ip6-localnet
ff00::0 ip6-mcastprefix
ff02::1 ip6-allnodes
ff02::2 ip6-allrouters
```
配置完hosts，sudo reboot重启生效

4.禁用防火墙
```
//关闭iptables
service iptables stop
systemctl disable iptables

//关闭selinux
getenforce //查看状态

vim /etc/selinux/config
修改为
SELINUX=disabled

//关闭firewalld
systemctl stop firewalld
systemctl disable firewalld

```

5.SSH登录配置
```
$ vim /etc/ssh/sshd_config
# 修改
UseDNS no
PermitRootLogin yes #允许root登录
PermitEmptyPasswords no #不允许空密码登录
PasswordAuthentication yes # 设置是否使用口令验证
```

6.关闭swap空间
```
swapoff -a
sed -ie '/swap/ s/^/# /' /etc/fstab 
free -m
```

7.配置桥接
```
cat > /etc/sysctl.d/k8s.conf << EOF
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
```

8.安装docker
参考[ubuntu下安装docker](./install-docker.md)


9.安装k8s
参考[ubuntu下安装k8s](./install-k8s.md)


执行完上述操作后，将虚拟机关机，选择刚才配置好虚拟机，打开VMware虚拟机菜单栏-》虚拟机-》快照-》拍摄快照，创建完快照后打开快照管理-》选择刚才创建好的快照-》点击克隆-》下一页-》现有快照-》创建完整克隆-》设置虚拟机名称为node1->点击完成。等待克隆完成，再重复操作克隆另外一台node2虚拟机。2台虚拟机克隆完毕后，需要按照步骤2中配置网络，设置node1和node2静态ip及dns分别为192.168.123.132和192.168.123.133。分别在三台虚拟机中ping 各自的ip地址，网络通则配置成功
最后三台主机分别为
master -> 192.168.123.131
node1  -> 192.168.123.132
node2  -> 192.168.123.133

### 集群配置

