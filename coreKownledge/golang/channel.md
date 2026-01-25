### 无缓冲通道异常的几种情况
- #### 在同一个协程中读写数据，会死锁
```golang
package main

func main() {
	ch1 := make(chan int)
	ch1<-1 //死锁,向无缓冲通道发送数据时，必须有其他协程从该通道读取数据，此处会一直阻塞
	<-ch1 //不会执行
    close(ch1) //不会执行
}
```

- #### 向已关闭通道写数据，会引发panic
```golang
package main

func main() {
	ch1 := make(chan int)
	close(ch1)
	ch1<-1 // panic，不能向关闭的通道写数据
}
```

- #### 重复关闭通道，会引发panic
```golang
package main

func main() {
	ch1 := make(chan int)
	close(ch1)
    close(ch1) //panic,不能关闭已经关闭过的通道
}
```

- #### 写或读未初始化通道会导致死锁
```golang
package main

func main() {
	var ch1 chan int
	go func() {
		ch1 <- 1 //对nil通道发送数据会导致永久阻塞，死锁
	}()
	<-ch1 //从nil通道读取数据会导致永久阻塞，死锁
}
```


- #### 写缓存已满的通道会导致死锁
```golang
package main

func main() {
	ch1 := make(chan int, 1)
	ch1 <- 1
	ch1 <- 2 //死锁，因为缓存已满且没有协程读，导致写永久阻塞，最终死锁
	close(ch1)
}
```

- #### 读没有数据的通道会导致死锁
```golang
package main

func main() {
	ch1 := make(chan int, 1)
	<-ch1 //永久阻塞，因为没有协程写
}
```

- #### 读已经关闭的通道，会返回该通道类型的0值
```golang
package main

import "fmt"

func main() {
	ch1 := make(chan int, 1)
	close(ch1)
	fmt.Println(<-ch1)  //返回0，不会报错
}
```

- #### range遍历通道时没有关闭通道会导致死锁
```
package main

import "fmt"

func main() {
	ch1 := make(chan int)
	go func() {
		ch1 <- 1
		ch1 <- 2
		
	}()

	for v := range ch1 { //死锁，因为for循环无法结束
		fmt.Println(v)
	}
	// for {
	// 	v, ok := <-ch1
	// 	if ok == false {
	// 		break
	// 	}
	// 	fmt.Println(v)
	// }
}

```

- #### 协程执行时序图
```
package main

import "fmt"

func main() {
	ch1 := make(chan int)
	go func() {
		ch1 <- 1
		ch1 <- 2
	}()
	fmt.Println(<-ch1)
	close(ch1)
}
```
根据happens-before 规则，分析下这段代码执行流程这段代码使用的是无缓冲 channel（make(chan int) 没有指定容量），所以它的行为完全受 Go 内存模型 中关于 unbuffered channel 的 happens-before 规则支配。
官方规则（来自 https://go.dev/ref/mem）最关键的两条：

A send on a channel happens before the corresponding receive from that channel completes.
（发送操作发生在对应的接收完成之前）
A receive from an unbuffered channel happens before the send on that channel completes.
（从无缓冲 channel 的接收发生在对应的发送完成之前）

这两条规则结合在一起，对无缓冲 channel 形成了严格的双向同步。
代码执行流程（按 happens-before 顺序分析）
Gopackage main

import "fmt"

func main() {
    ch1 := make(chan int)           // 无缓冲 channel

    go func() {                     // 新 goroutine G2 启动
        ch1 <- 1                    // G2: 发送 1（阻塞等待接收者）
        // 这一行只有在 main 完成 <-ch1 后才会继续执行
        ch1 <- 2                    // G2: 发送 2（再次阻塞等待接收者）
    }()                             // G2 现在运行到 ch1 <- 1 阻塞

    fmt.Println(<-ch1)              // main: 接收（阻塞等待发送者）
    // 这一行完成时，happens-before 保证：G2 的 ch1 <- 1 已完成
    // 同时：G2 的 ch1 <- 1 之后的代码可以继续执行

    close(ch1)                      // main: 关闭 channel
    // main 结束，整个程序退出
}
详细的 happens-before 时序（最可能/规范的执行路径）

main goroutine (G1) 创建 channel → 启动匿名 goroutine (G2)
G2 运行到 ch1 <- 1 → 因为无缓冲且无人接收 → G2 阻塞
G1 运行到 fmt.Println(<-ch1) → 因为无缓冲且无人发送 → G1 阻塞
Go runtime 调度器看到两个 goroutine 都在等待对方 → 发生同步配对（rendezvous）
关键同步点完成：
G1 的 <-ch1 操作完成（receive completes）
此时 happens-before：G2 的 ch1 <- 1 操作完成（send completes）
G1 从 channel 读取到值 1 并打印 1

因为第 2 条规则（unbuffered 特有）：
G1 的接收完成 happens-before G2 的发送完成
所以 G2 被唤醒，可以继续执行 ch1 <- 1之后的代码

G2 继续执行 → 到达 ch1 <- 2 → 再次阻塞（因为无人接收）
G1 执行 close(ch1)
main goroutine 结束 → 程序终止
此时 G2 还阻塞在 ch1 <- 2 上，但因为主 goroutine 已退出，runtime 会直接终止所有 goroutine（不等待）


输出结果
text1
程序结束（可能伴随 goroutine leaked 的警告，如果开启 race detector 或在某些严格模式下）。
为什么只打印 1？为什么第 2 个发送永远不会被读取？

main 只做一次接收 <-ch1
接收完成只“解锁”了第一个发送 ch1 <- 1
第二个发送 ch1 <- 2 需要另一次接收才能完成，但 main 已经没有再接收了
所以 G2 永远卡在第二个发送上
main 关闭 channel 后直接退出 → G2 被强制终止

happens-before 带来的内存可见性保证（最重要的一点）
当 main 收到 1 并打印时，根据规则：

G2 在 ch1 <- 1 之前的所有写操作（如果有）都 happens-before main 的接收完成
所以 main goroutine 必然能看到 G2 在发送 1 之前的所有内存修改

这就是无缓冲 channel 常被用来做严格同步 + 传递内存可见性的原因。

