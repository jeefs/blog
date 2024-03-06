### 一,简单介绍
使用场景:
- 服务器管理

优点:
- 降低运维人员对海量主机运维难度
- 自动化运维,提高运维效率

工作模式:
- C/S架构软件，分为控制端与受控端，两端通过ssh协议通信，通信格式为YAML编写的配置文件


### 二,安装ansible
先决条件:
- 已安装ubuntu18.04发行版本的linux主机
- 已设置可通过SSH访问服务器

```
//添加软件源
sudo apt-add-repository ppa:ansible/ansible

//更新源
sudo apt update

//执行安装
sudo apt install ansible
```


//安装完成后配置受控端主机ip
```
vim /etc/ansible/hosts

加入配置
[servers]
k8s-node1-server ansible_host=192.168.123.132  #指定受控端ip
k8s-node2-server ansible_host=192.168.123.133

[all:vars]
ansible_python_interpreter=/usr/bin/python3  //指定受控端python版本
```

连通性测试:
```
ansible all -m ping -u mike

//返回结果:
k8s-node1-server | UNREACHABLE! => {
    "changed": false,
    "msg": "Failed to connect to the host via ssh: mike@192.168.123.132: Permission denied (publickey,password).",
    "unreachable": true
}
k8s-node2-server | UNREACHABLE! => {
    "changed": false,
    "msg": "Failed to connect to the host via ssh: mike@192.168.123.133: Permission denied (publickey,password).",
    "unreachable": true
}
```

提示无法连接受控端，无权限，这里的原因是控制端没有添加受控端的ssh密钥,在控制端添加即可
```
ssh-keygen  //生成公钥

ssh-copy-id mike@192.168.123.132 //添加受控端1认证信息

ssh-copy-id mike@192.168.123.133 //添加受控端2认证信息 
```

再次执行ansible all -m ping -u mike，输出联通成功信息
```
k8s-node1-server | SUCCESS => {
    "changed": false,
    "ping": "pong"
}
k8s-node2-server | SUCCESS => {
    "changed": false,
    "ping": "pong"
}
```

### 三,作业
//todo


参考:
- https://www.digitalocean.com/community/tutorials/how-to-install-and-configure-ansible-on-ubuntu-18-04
