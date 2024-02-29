### 开启服务
确保clash System Proxy开关和Allow LAN开关处于开启状态

### 配置虚拟机代理
查看宿主机本机上网IP，可以在网卡信息里面查看

设置http和https和socks5代理地址为 宿主机IP:代理端口
```
cat >> ~/.bashrc << EOF
export https_proxy=http://192.168.50.105:7890
export http_proxy=http://192.168.50.105:7890
export all_proxy=socks5://192.168.50.105:7890
EOF
source ~/.bashrc
```

