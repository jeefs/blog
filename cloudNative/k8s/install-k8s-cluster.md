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
      #网关地址(虚拟机可以通过NAT查看网关IP)
      gateway4: 192.168.123.2
      #dsn服务器地址,设置为网关IP和公用DNS
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

8.安装containerd容器运行时
参考[ubuntu下安装containerd](./install-containerd.md)


9.安装k8s
参考[ubuntu下安装k8s](./install-k8s.md)


执行完上述操作后，将虚拟机关机，选择刚才配置好虚拟机，打开VMware虚拟机菜单栏-》虚拟机-》快照-》拍摄快照，创建完快照后打开快照管理-》选择刚才创建好的快照-》点击克隆-》下一页-》现有快照-》创建完整克隆-》设置虚拟机名称为node1->点击完成。等待克隆完成，再重复操作克隆另外一台node2虚拟机。2台虚拟机克隆完毕后，需要按照步骤2中配置网络，设置node1和node2静态ip及dns分别为192.168.123.132和192.168.123.133。分别在三台虚拟机中ping 各自的ip地址，网络通则配置成功
最后三台主机分别为
master -> 192.168.123.131
node1  -> 192.168.123.132
node2  -> 192.168.123.133

### 集群配置
1.初始化master节点
```
// apiserver-advertise-address 为master主机ip
// image-repository 为镜像地址
// kubernetes-version 为k8s安装的版本号
// service-cidr 为服务器地址
// pod-network-cidr 为pod 内部网络地址段
kubeadm init \
  --apiserver-advertise-address=192.168.123.131 \
  --image-repository registry.aliyuncs.com/google_containers \
  --kubernetes-version v1.28.2 \
  --service-cidr=10.96.0.0/12 \
  --pod-network-cidr=10.244.0.0/16 \
  --ignore-preflight-errors=all
```
当初始化master时可以使用kubeadm reset清理缓存，解决失败原因后重新初始化

执行初始化成功后会提示,继续执行如下命令
```
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```
还会生成密钥,在两外两台主机上分别执行，即可让node节点加入
```
kubeadm join 192.168.123.131:6443 --token dquo1s.5dlohkhucp01gu8p \
        --discovery-token-ca-cert-hash sha256:072f078cffef717c8e07e31c145129c85b4573624b079f214a58e2fc19748b0e
```

### 安装CNI插件calico
```
//安装calico依赖说明：https://docs.tigera.io/calico/latest/getting-started/kubernetes/self-managed-onprem/onpremises

//此步骤仅在master执行即可
wget https://raw.githubusercontent.com/projectcalico/calico/v3.25.0/manifests/calico.yaml

//修改配置文件
将
# - name: CALICO_IPV4POOL_CIDR
#   value: "192.168.0.0/16"
修改为
- name: CALICO_IPV4POOL_CIDR
  value: "10.244.0.0/16"
//CALICO_IOPV4POOL_CIDR必须和kubeadm初始化时--pod-network-cdr保持一致


//应用calico配置，安装
kubectl apply -f calico.yaml

//验证查看node注册状态，当为 Ready 时表示网络插件安装完成
kubectl get node
NAME       STATUS   ROLES           AGE   VERSION
stache31   Ready    control-plane   4d    v1.26.2
stache32   Ready    <none>          4d    v1.26.2
stache33   Ready    <none>          4d    v1.26.2

//验证查看node注册状态，当为 Ready 时表示网络插件安装完成
kubectl get node
NAME       STATUS   ROLES           AGE   VERSION
stache31   Ready    control-plane   4d    v1.26.2
stache32   Ready    <none>          4d    v1.26.2
stache33   Ready    <none>          4d    v1.26.2
```

参考:
- https://zhuanlan.zhihu.com/p/612051521?utm_id=0
- https://jasonkayzk.github.io/2021/05/16/%E5%9C%A8VMWare%E4%B8%AD%E9%83%A8%E7%BD%B2%E4%BD%A0%E7%9A%84K8S%E9%9B%86%E7%BE%A4/
