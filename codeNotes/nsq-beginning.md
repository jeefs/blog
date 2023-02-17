### 消息队列的常见使用场景
- 异步处理：耗时业务场景，避免客户端超时，提高系统吞吐量
- 应用解耦：不同业务之间通过消息队列交互，减少耦合
- 流量削锋：流量过大的场景，先写入队列，队列满后，后续请求抛弃，保证服务稳定性
- 日志处理：日志写入队列，日志消费者通过订阅的方式取出日志处理

### 消息队列的两种模式
- 点对点[Queue]
- 发布/订阅[Topic]

1.点对点：消息发送到队列中，只能被一个消费者消费，消费者不会消费到重复的消息

2.发布/订阅: 消息发送到队列中，可以被多个消费者订阅

3.分组订阅: 消息发送到topic中，多个消费者组成订阅组订阅queue

### nsq 架构图
![](https://github.com/liangjfblue/liangjfblue.github.io/blob/master/img/post_nsq_jaigou.png?raw=true)
nsq有3个守护程序：
- nsqd
- nsqlookupd
- nsqadmin

1.nsqd的作用是接收，排队，发送消息给客户端的守护程序

2.nsqlookupd的作用是提供nsqd发现功能，每个消费者都可以通过其获取已经存在的nsqd信息，相当于服务发现和注册中心

3.nsqadmin的作用是一个网页用户界面，可以实时展示节点和集群的信息

### 注意事项
1.每个topic(队列)里面的消息都会被拷贝到不同的channel（管道）中，但同一个消息只会被随机指派到订阅channel的某一个消费者消费


资料引用
- https://developer.aliyun.com/article/681466
- https://cloud.tencent.com/developer/article/1558847
- https://nsq.io/overview/design.html
