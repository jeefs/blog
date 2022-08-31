# 查询表是否存在某个字段
select count(*) from information_schema.columns where table_name = '表名' and column_name = '字段名'

select count(*)  as columns_existed,TABLE_NAME from information_schema.`COLUMNS` where TABLE_NAME  like "cgm_glucose_%"  and COLUMN_NAME = "deleted_at" group by TABLE_NAME 

# 获取数据库所有表名
select table_name from information_schema.tables where table_schema='crmv1' and table_type='base table' having table_name

# 获取表的所有字段名
select column_name from information_schema.columns where table_schema='csdb' and table_name='users'

# 按字段统计
select count(name),name from cgm_glucose_2207 group by name 

# 按时间分别取重复记录中最早一条
select * from devices_cgm_info a where created_at = (select MIN(created_at) from devices_cgm_info where mac = a.mac)  and a.`type`  = 1 order by a.created_at asc

# 判断某个字段值是否为数字类型
select * from cgm_glucose_2108 where mac = 'D6:23:17:79:DA:95' and (currentWarning REGEXP '(^[0-9]+.[0-9]+$)|(^[0-9]$)') = 0 order by `index` asc

# 批量插入数据


# 批量生成修改表字段sql
select CONCAT('ALTER TABLE crmv1.',table_name,' ADD client_create_time BIGINT NULL COMMENT',' \'客户端数据创建时间\'; ')  
from information_schema.tables where TABLE_NAME  like "cgm_glucose_%" and TABLE_SCHEMA  = "crmv1"

#批量生成删除标字段sql
select CONCAT('ALTER TABLE crmv1.',table_name,' DROP COLUMN deleted_at;')  
from (select count(*)  as columns_existed,TABLE_NAME from information_schema.`COLUMNS` where TABLE_NAME  like "cgm_glucose_%"  
and COLUMN_NAME = "deleted_at" group by TABLE_NAME ) as tmp
