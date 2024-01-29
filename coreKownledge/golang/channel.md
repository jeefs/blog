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

for range 遍历channel时有几点需要注意:
1.即使通道被关闭，for range会遍历完所有元素后才结束代码运行
2.如果通道的值为nil,则for range会panic
3.必须要关闭chan，否则for range会panic
func IterateOverTheChans(wg *sync.WaitGroup) {
	ch1 := make(chan int, 5)
	go func() {
		for i := 1; i <= 5; i++ {
			ch1 <- i
			fmt.Printf("发送数据%v\n", i)
			fmt.Println("发送等待1秒钟")
			time.Sleep(1 * time.Second)
		}
		close(ch1)
		wg.Done()
	}()

	go func() {
		for v := range ch1 {
			fmt.Printf("接受数据%v\n", v)
		}
		fmt.Println("所有元素接受完毕")
		wg.Done()
	}()
}

/*
select使用时需要注意:
1.分支条件阻塞视为条件不满足
2.如果没有条件满足，有default分支，则执行，无default分支则select会阻塞直到有条件满足时才执行
3.select只会对各分支求值一次，如果需要轮询则需要配合for使用，注意在select中使用break只会跳出select,而不会跳出外层for
*/
func SelectChan() {
	initChannels := [3]chan int{
		make(chan int, 1),
		make(chan int, 1),
		make(chan int, 1),
	}
	rand.Seed(time.Now().UnixNano())
	index := rand.Intn(3)
	fmt.Println(index)
	initChannels[index] <- index
	select {
	case <-initChannels[0]:
		fmt.Println("first value selected")
	case <-initChannels[1]:
		fmt.Println("second value selected")
	case <-initChannels[2]:
		fmt.Println("third value selected")
	default:
		fmt.Println("no selected")
	}
}

// 可以利用goto语句跳出for select语句
func BreakSelectChan(wg *sync.WaitGroup) {
	ch1 := make(chan int, 1)
	go func() {
		for i := 1; i <= 10; i++ {
			ch1 <- i
		}
		close(ch1)
		fmt.Println("sender:close chan")
		wg.Done()
	}()

	go func() {
		for {
			select {
			case v, ok := <-ch1:
				if ok == false {
					fmt.Println("receiver:close chan")
					goto LOOP
				}
				fmt.Printf("receiver: the value is %v\n", v)
			}
		}
	LOOP:
		fmt.Println("receiver:all data received")
		wg.Done()
	}()
}


```
