### 介绍
nats是一款开源消息队列，包含2大组件
- core-nats: 不做持久化的及时信息传输系统
- nats-jetstream: 基于 nats 的持久化消息队列

支持3种模型
- 发布订阅(一个发布者, 多个订阅者, 多个订阅者都可以收到同一个消息)

- 队列模型(队列模型, 一个发布者, 多个订阅者, 消息在多个消息中负载均衡分配, 分配给 A 消费者, 这个消息就不会再分配给其他消费者了)

- 请求响应(生产者能收到消费者的回复)

### 安装
[nats-server下载链接](https://github.com/nats-io/nats-server/releases/)
解压后使用supervisor 启动服务器

先安装supervisor，并且保证supervisor服务启动，添加nats配置文件
``` ini
[group:nats]
programs=nats-server

[program:nats-server]
command=/home/mike/nats-server/nats-server -m 8222
directory=/home/mike/nats-server
stdout_logfile=/var/log/supervisor/nats_server_stdout.log
stderr_logfile=/var/log/supervisor/nats_server_stderr.log
autostart=true
autorestart=true
stopasgroup=true
killasgroup=true
user=root
environment=

```

确保nats-server启动后，可以接入客户端
[nats-client下载链接](https://github.com/nats-io/nats.go)

demo
``` go
import (
	"fmt"
	"github.com/nats-io/nats.go"
	"os"
	"os/signal"
	"sync"
	"syscall"
)
const TASK_NAME = "defaultTask"

func main() {
    initOnce.Do(func() {
        go consumer()
    })
}

func consumer() {
    nc, err := nats.Connect(nats.DefaultURL)
    if err != nil {
    err = fmt.Errorf("NATS subscribe %v failed:%w", TASK_NAME, err)
        panic(err)
    }
    _, err = nc.Subscribe(TASK_NAME, func(msg *nats.Msg) {
	    //todo handler msg	
    })
    if err != nil {
        err = fmt.Errorf("NATS subscribe %v failed:%w", TASK_NAME, err)
        panic(err)
    }
    quit := make(chan os.Signal)
    signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
    fmt.Println("NATS subscribe state: awaiting signal")
    sig := <-quit
    fmt.Printf("NATS subscribe state: received signal [%v]", sig)
    nc.Drain()
    nc.Close()
}

func producer() {
    nc, err := nats.Connect(nats.DefaultURL)
    defer nc.Close()
    if err != nil {
        fmt.Println(err)
    }
    err = nc.Publish(TASK_NAME, msg)
	if err != nil {
	    fmt.Println(err)
    }
}

```

reference:

- <https://docs.nats.io/nats-concepts/jetstream/streams>
- <https://juejin.cn/post/7152084392628715551>







