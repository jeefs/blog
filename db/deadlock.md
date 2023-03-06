### 死锁
在并发计算中，死锁是指一些实体组中的任何成员都不能继续进行的任何情况，因为每个成员都在等待另一个成员，包括自己，采取行动，如发送消息或更常见的释放锁。[1] 死锁是多处理系统、并行计算和分布式系统的常见问题，因为在这些情况下，系统经常使用软件或硬件锁来仲裁共享资源和实现进程同步。

在操作系统中，当一个进程或线程进入等待状态时，就会发生死锁，因为所请求的系统资源被另一个等待中的进程所占有，而这个进程又在等待另一个等待中的进程所占有的资源。

在通信系统中，死锁的发生主要是由于信号的丢失或损坏，而不是对资源的争夺[5]。

### 死锁日志分析
```
LATEST DETECTED DEADLOCK
------------------------
2023-03-03 08:20:22 0x7fdd4123c700
*** (1) TRANSACTION:
TRANSACTION 137938756, ACTIVE 0 sec starting index read
mysql tables in use 1, locked 1
LOCK WAIT 2 lock struct(s), heap size 1136, 1 row lock(s)
MySQL thread id 3753, OS thread handle 140588575119104, query id 81593849 localhost 127.0.0.1 demo updating
DELETE FROM `experiment_result` WHERE experiment_result.experiment_id = ?
*** (1) WAITING FOR THIS LOCK TO BE GRANTED:
RECORD LOCKS space id 357 page no 3 n bits 136 index PRIMARY of table `crmv1`.`experiment_result` trx id 137938756 lock_mode X waiting
Record lock, heap no 3 PHYSICAL RECORD: n_fields 16; compact format; info bits 0
 0: len 30; hex 30333861393531382d303938382d346662352d623562622d663233663362; asc 038a9518-0988-4fb5-b5bb-f23f3b; (total 36 bytes);
 1: len 6; hex 0000083677f0; asc    6w ;;
 2: len 7; hex e2000001f5016a; asc       j;;
 3: len 4; hex 640072ec; asc d r ;;
 4: len 4; hex 640072ec; asc d r ;;
 5: len 30; hex 61373764393161382d663263322d346238382d393333362d656139383134; asc a77d91a8-f2c2-4b88-9336-ea9814; (total 36 bytes);
 6: len 8; hex 8000000000000001; asc         ;;
 7: len 6; hex 2d32332e3238; asc -23.28;;
 8: len 6; hex 31372e323425; asc 17.24%;;
 9: len 6; hex 31372e323425; asc 17.24%;;
 10: len 6; hex 37362e343725; asc 76.47%;;
 11: len 6; hex 39362e303825; asc 96.08%;;
 12: len 8; hex 8000000000000000; asc         ;;
 13: len 10; hex 37392e322d3139392e38; asc 79.2-199.8;;
 14: len 8; hex 8000000000000049; asc        I;;
 15: len 8; hex 8000000000000033; asc        3;;


*** (2) TRANSACTION:
TRANSACTION 137938754, ACTIVE 0 sec inserting
mysql tables in use 1, locked 1
4 lock struct(s), heap size 1136, 41 row lock(s), undo log entries 7
MySQL thread id 3726, OS thread handle 140588257363712, query id 81593856 localhost 127.0.0.1 demo update
INSERT INTO `experiment_result` (`id`,`experiment_id`,`last_update_time`,`total`,`totalPair`,`pair`,`avg_deviation`,`mard`,`median_mard`,`sign_20`,`sign_40`,`column_tag`,`created_at`,`updated_at`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?),(?,?,?,?,?,?,?,?,?,?,?,?,?,?),(?,?,?,?,?,?,?,?,?,?,?,?,?,?),(?,?,?,?,?,?,?,?,?,?,?,?,?,?)
*** (2) HOLDS THE LOCK(S):
RECORD LOCKS space id 357 page no 3 n bits 136 index PRIMARY of table `crmv1`.`experiment_result` trx id 137938754 lock_mode X
Record lock, heap no 1 PHYSICAL RECORD: n_fields 1; compact format; info bits 0
 0: len 8; hex 73757072656d756d; asc supremum;;
 
Record lock, heap no 3 PHYSICAL RECORD: n_fields 16; compact format; info bits 0
 0: len 30; hex 30333861393531382d303938382d346662352d623562622d663233663362; asc 038a9518-0988-4fb5-b5bb-f23f3b; (total 36 bytes);
 1: len 6; hex 0000083677f0; asc    6w ;;
 2: len 7; hex e2000001f5016a; asc       j;;
 3: len 4; hex 640072ec; asc d r ;;
 4: len 4; hex 640072ec; asc d r ;;
 5: len 30; hex 61373764393161382d663263322d346238382d393333362d656139383134; asc a77d91a8-f2c2-4b88-9336-ea9814; (total 36 bytes);
 6: len 8; hex 8000000000000001; asc         ;;
 7: len 6; hex 2d32332e3238; asc -23.28;;
 8: len 6; hex 31372e323425; asc 17.24%;;
 9: len 6; hex 31372e323425; asc 17.24%;;
 10: len 6; hex 37362e343725; asc 76.47%;;
 11: len 6; hex 39362e303825; asc 96.08%;;
 12: len 8; hex 8000000000000000; asc         ;;
 13: len 10; hex 37392e322d3139392e38; asc 79.2-199.8;;
 14: len 8; hex 8000000000000049; asc        I;;
 15: len 8; hex 8000000000000033; asc        3;;

Record lock, heap no 5 PHYSICAL RECORD: n_fields 16; compact format; info bits 0
 0: len 30; hex 30643766643635632d643739362d346363372d383663362d316435346362; asc 0d7fd65c-d796-4cc7-86c6-1d54cb; (total 36 bytes);
 1: len 6; hex 0000083677f0; asc    6w ;;
 2: len 7; hex e2000001f5013d; asc       =;;
 3: len 4; hex 640072ec; asc d r ;;
 4: len 4; hex 640072ec; asc d r ;;
 5: len 30; hex 61373764393161382d663263322d346238382d393333362d656139383134; asc a77d91a8-f2c2-4b88-9336-ea9814; (total 36 bytes);
 6: len 8; hex 8000000000000001; asc         ;;
 7: len 6; hex 2d32322e3937; asc -22.97;;
 8: len 6; hex 31362e333825; asc 16.38%;;
 9: len 6; hex 31372e303525; asc 17.05%;;
 10: len 6; hex 37362e333925; asc 76.39%;;
 11: len 6; hex 39372e323225; asc 97.22%;;
 12: len 8; hex 8000000000000000; asc         ;;
 13: len 3; hex 616c6c; asc all;;
 14: len 8; hex 8000000000000049; asc        I;;
 15: len 8; hex 8000000000000048; asc        H;;

```
业务中存在并发操作数据库，具体为两个或多个不同事物，先通过非唯一性字段删除数据，再新增记录
引用：
https://en.wikipedia.org/wiki/Deadlock
https://www.aneasystone.com/archives/2017/11/solving-dead-locks-two.html
https://www.aneasystone.com/archives/2018/04/solving-dead-locks-four.html
https://en.wikipedia.org/wiki/Isolation_(database_systems)
https://blog.csdn.net/hellozhxy/article/details/88052296