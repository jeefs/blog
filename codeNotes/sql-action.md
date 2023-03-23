### sql优化
-  索引
1.组合索引，遵循最左原则，例如 index(a,b,c) 只有 a,ab,abc 三种条件组合才能使用到索引,创建索引时要考虑字段使用频率，可以参考Cardinality索引基数(索引中不重复记录的预估值,越大越好)
2.索引覆盖（结果集字段全部都是索引）能使用索引，提高查找速度

- 主键
1.自增主键优点大于缺点，在检索上非常快，主键索引适合查询整行记录时使用，在具体业务时可以通过先通过普通索引查出对应行的主键id，再通过id找到完整记录，一般情况下能获得最好的性能


- 实际场景下的索引命中优化
1.表A用户表  有字段 id,name,phone
2.表B签到表  有字段 id,uid,ctime,remark


需求1：找出按签到时间倒序的前10用户信息并包含签到信息
方案:
```
 select id from A order by created_at  desc limit 10  //created_at 建立普通索引
 select * from B where id  in( select id from B where uid = 1)  union all select * from B where id  in( select id from B where uid = 1)  //uid建立普通索引，将第一步获取的id集合组成union all语句，能高效的使用主键索引，合并开销可以忽略不记
通过业务代码将用户信息和签到信息组合
 疑问: 为什么不写成 select * from B where uid in(select id from A order by created_at  desc limit 10 )  //原因有2种，第一in子查询不支持order by和limit 关键字，第二,in 子句在某些条件下会索引失效
 
关键点: 在查询整行记录时，将普通字段条件转化为主键查询，将负责的关联查询拆分成单句查寻配合索引能提高索引命中率及查询性能
```


需求2：千万级别的记录需要统计总数
方案:
mysql对 count(*)有优化，默认情况下会选择字符占用最小的字段进行count，可以新建一个tinyint(1) 字段，建立普通索引，在count(*)时会使用到









