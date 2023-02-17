### 安装nsq
1.从官方github仓库拉取二进制文件
```
git clone https://github.com/nsqio/nsq.git
```
下完解压后，进入bin目录，下面有几个文件分别是
- nsqadmin webUI管理文件
- nsqd  nsqd服务文件
- nsqlookupd   nsqlookupd服务文件
- nsq_stat  
- nsq_tail  
- nsq_to_file  将消费者的消息写入文件，测试用
- nsq_to_http  
- nsq_to_nsq 
- to_nsq

2.服务开启（使用nohup部署）
```
nohup ./nsqlookupd /dev/null > nohup.nsqlookupd.out 2>&1 &
nohup ./nsqd --lookupd-tcp-address=127.0.0.1:4160 > nohup.nsqd.out 2>&1 &  绑定注册和服务中心的ip协议端口
nohup ./nsqadmin --lookupd-http-address=127.0.0.1:4161 > nohup.nsqadmin.out 2>&1 & 绑定注册和服务中心的http协议端口
```

3.实验



部署集群
