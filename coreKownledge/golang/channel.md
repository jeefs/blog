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



