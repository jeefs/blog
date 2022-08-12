#查询表是否存在某个字段
select count(*) from information_schema.columns where table_name = '表名' and column_name = '字段名'

#获取数据库所有表名
select table_name from information_schema.tables where table_schema='crmv1' and table_type='base table' having table_name

#获取表的所有字段名
select column_name from information_schema.columns where table_schema='csdb' and table_name='users'

#按字段统计
select count(name),name from cgm_glucose_2207 group by name 

#按时间分别取重复记录中最早一条
select * from devices_cgm_info a where created_at = (select MIN(created_at) from devices_cgm_info where mac = a.mac)  and a.`type`  = 1 order by a.created_at asc

#判断某个字段值是否为数字类型
select * from cgm_glucose_2108 where mac = 'D6:23:17:79:DA:95' and (currentWarning REGEXP '(^[0-9]+.[0-9]+$)|(^[0-9]$)') = 0 order by `index` asc

#批量插入数据

