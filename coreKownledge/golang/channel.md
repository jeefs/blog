### 通道的基本特性
- 对于同一个通道，发送操作之间是互斥的，接收操作之间也是互斥的
- 发送操作和接收操作中对元素值的处理都是不可分割的
- 发送操作在完全完成之前会被阻塞。接收操作也是如此


### 发送和接受的步骤
- 发送:复制元素值，放置副本到通道内部
- 接受:复制元素值，放置副本到接受方，删除原值

### 通道被阻塞或导致panic的几种情况
```
package channel

import (
	"fmt"
	"sync"
)

// 向nil chan发送数据或接收数据会死锁
func NilChanBlocking() {
	var ch1 chan int
	//ch1 <- 1 //死锁
	<-ch1 //死锁
}

// 向缓冲满的chan写数据会阻塞
func FullChanBlocking() {
	ch1 := make(chan int, 1)
	ch1 <- 1
	ch1 <- 2 //阻塞，缓冲已满，不能写
}

// 向缓冲空的chan读数据会阻塞
func EmptyChanBlocking() {
	ch1 := make(chan int, 1)
	ch1 <- 1
	<-ch1
	<-ch1 //阻塞，缓冲已空，不能读
}

// 无缓冲chan，写时，读未就绪panic
func NoReceiverChanPanic() {
	ch1 := make(chan int)
	ch1 <- 1 //panic，无缓冲chan，写时，读必须就绪
}

// 无缓冲chan，读时，写未就绪panic
func NoSenderChanPanic() {
	ch1 := make(chan int)
	v := <-ch1 //panic，无缓冲chan,读时，写必须就绪
	fmt.Println(v)
}

// 向已关闭的chan写数据会panic
func ClosedChanPanic() {
	ch1 := make(chan int, 1)
	close(ch1)
	ch1 <- 1 //panic,不能写已关闭的chan
}

// 关闭已关闭的chan会panic
func ClosedChanAgainClosePanic() {
	ch1 := make(chan int, 1)
	close(ch1)
	close(ch1) //panic,不能关闭已关闭的chan
}

// 读chan可狭义判断chan是否关闭，如果chan关闭时里面还有未取出的值，则第二个值还是true，通过此方式判断chan是否关闭有延迟
func IsChanClosed() {
	ch1 := make(chan int, 2)
	ch1 <- 1
	close(ch1)
	v, ok := <-ch1 //此时chan还有值未取出，v为true,但chan已经关闭
	if !ok {
		fmt.Println("chan closed1")
	}
	fmt.Println(v)
}

//可用死循环来轮询chan是否关闭
func SendAndReceiveMsg(wg *sync.WaitGroup) {
	ch := make(chan int, 10)
	go func() {
		for i := 0; i < cap(ch); i++ {
			data := i + 1
			ch <- data
			fmt.Printf("sender: send data %v\n", data)
		}
		close(ch)
		fmt.Println("sender: chan closed")
		wg.Done()
	}()

	go func() {
		for {
			v, ok := <-ch
			if false == ok {
				fmt.Println("receiver: chan closed")
				break
			}
			fmt.Printf("receiver:receive data is %v\n", v)
		}
		fmt.Println("receiver exit")
		wg.Done()
	}()
}

```
