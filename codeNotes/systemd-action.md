### 介绍

systemd为Linux操作系统提供守护进程管理功能



### 配置文件定义

ubuntu配置文件默认在 /etc/systemd/system下

```shell
# admin.service
[Unit]
Description=admin  #定义单元名称

[Service]
User=nobody  
Type=simple  #类型，simple为执行命令
ExecStart=/bin/bash -c "cd /home/gaoy/cgm_manager/cmd/admin && ./admin"  #具体执行的命令路径
Restart=always #挂断是否重启
RestartSec=5 #挂断后重启间隔，5秒

[Install]
WantedBy=multi-user.target 
```



### systemctl 操作（提供服务管理）

``` shell
systemctl daemon-reload   #每次修改配置文件都需要执行重载命令

systemctl start admin.service #启动服务

systemctl restart admin.service # 重启服务

systemctl stop admin.service # 停止服务
```



### journalctl 操作(提供服务日志管理)

```shell
journalctl -u admin.service  #查看指定服务日志

journalctl -u admin.service -f #实时打印服务日志

journalctl --vacuum-time=10s #清理10秒前的日志

#日志存储大小，滚动时间，可以通/etc/systemd/journald.conf 配置文件定义

```





