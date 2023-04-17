### 日志目录
/var/log/redis/redis-server.log

### 安装目录
/usr/bin/redis-server

### 配置文件目录
/etc/redis/redis.conf


### 关闭RDB快照
注释掉原来的持久化规则
# save 3600 1
# save 300 100
# save 60 10000


设置save ""

删除已经存在的备份文件

rm -rf /var/lib/redis/dump.rdb

