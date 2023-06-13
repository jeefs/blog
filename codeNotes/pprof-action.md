### 概念
pprof是go官方提供的程序运行时信息收集与分析工具

### 安装
gin框架安装
```
go get https://github.com/gin-contrib/pprof
```

在main.go入口文件引入
```
 import ("github.com/gin-contrib/pprof")
 
 ginRouter := gin.Default() 
 pprof.Register(ginRouter) //注册路由
```

### 使用方法
启动服务后，可以通过http://服务域名/debug/pprof 访问信息面板会输入如下信息:
```
/debug/pprof/

Types of profiles available:
Count	Profile
620	allocs
0	block
0	cmdline
29	goroutine
620	heap
0	mutex
0	profile
11	threadcreate
0	trace
full goroutine stack dump
Profile Descriptions:

allocs: A sampling of all past memory allocations
block: Stack traces that led to blocking on synchronization primitives
cmdline: The command line invocation of the current program
goroutine: Stack traces of all current goroutines
heap: A sampling of memory allocations of live objects. You can specify the gc GET parameter to run GC before taking the heap sample.
mutex: Stack traces of holders of contended mutexes
profile: CPU profile. You can specify the duration in the seconds GET parameter. After you get the profile file, use the go tool pprof command to investigate the profile.
threadcreate: Stack traces that led to the creation of new OS threads
trace: A trace of execution of the current program. You can specify the duration in the seconds GET parameter. After you get the trace file, use the go tool trace command to investigate the trace.

block：goroutine的阻塞信息，本例就截取自一个goroutine阻塞的demo，但block为0，没掌握block的用法
goroutine：所有goroutine的信息，下面的full goroutine stack dump是输出所有goroutine的调用栈，是goroutine的debug=2，后面会详细介绍。
heap：堆内存的信息
mutex：锁的信息
threadcreate：线程信息
```
我们重点关注goroutine 这一项，点击进去，会显示近期收集的协程
http://服务域名/debug/pprof/goroutine?debug=1 ，或者debug=2显示更详细信息
```
goroutine profile: total 32
10 @ 0x43f836 0x44f6fc 0xa844ca 0x471081
#	0xa844c9	github.com/go-sql-driver/mysql.(*mysqlConn).startWatcher.func1+0xa9	/home/gaoy/go/pkg/mod/github.com/go-sql-driver/mysql@v1.6.0/connection.go:614

2 @ 0x43f836 0x40b93b 0x40b438 0xa16cd1 0x471081
#	0xa16cd0	database/sql.(*Tx).awaitDone+0x30	/usr/local/go/src/database/sql/sql.go:2187

2 @ 0x43f836 0x438037 0x46b069 0x4f5212 0x4f657a 0x4f6568 0x53f8e9 0x552925 0x6f259f 0x471081
#	0x46b068	internal/poll.runtime_pollWait+0x88		/usr/local/go/src/runtime/netpoll.go:305
#	0x4f5211	internal/poll.(*pollDesc).wait+0x31		/usr/local/go/src/internal/poll/fd_poll_runtime.go:84
#	0x4f6579	internal/poll.(*pollDesc).waitRead+0x259	/usr/local/go/src/internal/poll/fd_poll_runtime.go:89
#	0x4f6567	internal/poll.(*FD).Read+0x247			/usr/local/go/src/internal/poll/fd_unix.go:167
#	0x53f8e8	net.(*netFD).Read+0x28				/usr/local/go/src/net/fd_posix.go:55
#	0x552924	net.(*conn).Read+0x44				/usr/local/go/src/net/net.go:183
#	0x6f259e	net/http.(*connReader).backgroundRead+0x3e	/usr/local/go/src/net/http/server.go:678
```
也可以命令行方式获取信息
```
go tool pprof http://127.0.0.1:2027/debug/pprof/heap //下载相关信息并进入交互模式
进入交互模式后，可以输入命令筛选数据
top 10 //筛选出内存占用前10的函数

flat: 本函数占用的内存量。
flat%: 本函数内存占使用中内存总量的百分比。
sum%: 前面每一行flat百分比的和，比如第2行虽然的100% 是 100% + 0%。
cum: 是累计量，加入main函数调用了函数f，函数f占用的内存量，也会记进来。
cum%: 是累计量占总量的百分比。

-----------------------
list + 函数名称 //查看某个函数的代码，以及该函数每行代码的指标信息

-----------------------
traces //打印所有调用栈，以及调用栈的指标信息。
```

linux系统自带top 命令，输入后按大写M，可以根据内存占用排序，也能帮助分析问题

### 使用可视化工具分析耗时
```
//下载统计数据,采集30秒的数据
curl -o profile.out https://app.sibsensing.cn/debug/pprof/profile?seconds=30

//安装Graphviz
https://graphviz.org/download/

windows下需要将Graphviz\bin设置为环境变量,并且使用管理员权限运行bat控制台，进入安装目录下执行dot -c

//展示可视化图形
go tool pprof -http=:8000 profile.out
```
引用:
- https://lessisbetter.site/2019/05/18/go-goroutine-leak
- https://wudaijun.com/2018/04/go-pprof
- https://blog.csdn.net/pengpengzhou/article/details/107000659
- https://zhuanlan.zhihu.com/p/396363069
