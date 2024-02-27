### 系统环境
ubuntu 18.04 LTS

### 安装过程
```
//添加国内k8s源
cat <<EOF >/etc/apt/sources.list.d/kubernetes.list
deb https://mirrors.aliyun.com/kubernetes/apt/ kubernetes-xenial main
EOF

//导入密钥
curl https://mirrors.aliyun.com/kubernetes/apt/doc/apt-key.gpg | apt-key add -


//更新源
sudo apt update  


//执行安装
apt-get install -y kubelet kubeadm kubectl

//设置开机自启动
systemctl enable kubelet

//检查运行状态
systemctl status kubelet
```
