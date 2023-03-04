### supervisor 是python实现的进程管理管理工具，提供进程守护功能

### ubuntu下安装
 ```
 apt install supervisor
 ```
应用配置文件目录： 
```
/etc/supervisor/conf.d
```

配置文件格式:
```
[program:admin]
user=root
command=/bin/bash -c "cd /source/gsmv1/cmd/admin && ./admin" //执行的命令
stopsignal=TERM
autostart=true
autorestart=true
stdout_logfile=/source/gsmv1/cmd/admin/admin-stdout.log
stderr_logfile=/source/gsmv1/cmd/admin/admin-stderr.log
```

常用命令
```
service supervisor start   //启动supervisor进程
supervisorctl update // 修改完配置文件需要执行更新命令生效
supervisorctl start admin //启动应用
supervisorctl stop admin //暂停应用
supervisorctl status admin //查看状态
supervisorctl restart admin //重启应用
```
