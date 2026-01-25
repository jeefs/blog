### 无缓冲通道
```golang
package main

func main() {
	ch1 := make(chan int)
	ch1<-1 //死锁,向无缓冲通道发送数据时，必须有其他协程从该通道读取数据，此处会一直阻塞
	<-ch1 //不会执行
    close(ch1) //不会执行
}
```

