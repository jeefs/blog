### 简介
logrotate 是一个 linux 系统日志的管理工具。可以对单个日志文件或者某个目录下的文件按时间 / 大小进行切割，压缩操作；指定日志保存数量；还可以在切割之后运行自定义命令。

### 配置logrotate

logrotate 的配置文件默认存放在 /etc/logrotate.d
使用vim创建配置文件:
```
vim /etc/logrotate.d/supervisor-admin

添加内容:
/var/log/supervisor/admin_std*.log {
    daily
    rotate 30
    dateext
    dateyesterday
    copytruncate
    delaycompress
    compress
    missingok
    notifempty
}
```
logrotate会根据/var/log/supervisor/admin_std*.log匹配日志来备份,该应用的日志会被放到这个目录

- daily: 日志按天轮询。也可以设为weekly、monthly、yearly
- rotate ： 备份数，超过的会删除
- dateext: 备份文件名包含日期信息
- dateyesterday 用昨天的日期做后缀,因为日志一般是凌晨备份前一天的数据，如果不用这个参数，会造成，日志文件显示的日期和实际不是一天
- copytruncate： 首先将目标文件复制一份，然后在做截取（truncate）。这样做就防止了直接将原目标文件重命名引起的问题。
- delaycompress ：与compress选项一起用，delaycompress选项指示logrotate不将最近的归档压缩，压缩将在下一次轮循周期进行 就是最新两个日志文档不压缩
- compress： 压缩文件。如果不想压缩 可以和delaycompress 一起去掉
- missingok： 忽略错误
- notifempty： 如果没有日志 不进行轮询


### 配置supervisor

supervisor默认配置文件放在  /etc/supervisor/conf.d
使用vim修改配置文件:
```
vim /etc/supervisor/conf.d/admin.conf
添加内容:


# no limit on the size
stdout_logfile_maxbytes=0
stderr_logfile_maxbytes=0

# no backup with supervisor
stdout_logfile_backups=0
stderr_logfile_backups=0

stdout_logfile=/var/log/supervisor/admin_stdout.log
stderr_logfile=/var/log/supervisor/admin_stderr.log

```

上述4行配置作用是让supervisor自己不备份和限制日志大小，从而由logrotate接管

保存后执行更新配置文件:
```
supervisorctl update
supervisorctl restart admin
```


