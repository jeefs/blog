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

#### 协程执行时序图
在Happens-Before 规则的条件下，协程的执行结果可预测，但是执行顺序不可预测

#### 利用channel实现生产者-消费者模式
```golang
package main

import (
	"fmt"
	"sync"
)

/*
1.缓存满时，生产者阻塞，等待消费者消费
2.缓存空时，消费者阻塞，等待生产者写入
3.不同消费者间互斥，不能同时操作资源
4.消费者和生产者之间互斥，不能同时操作资源
写入操作必须有排他性和原子性
*/

var queue chan int

func main() {
	var wg sync.WaitGroup
	queue = make(chan int, 10)
	wg.Add(1)
	go producer(&wg)
	for i := 1; i <= 3; i++ {
		wg.Add(1)
		go consumer(i, &wg)
	}
	wg.Wait()
}

func producer(wg *sync.WaitGroup) {
	for i := range 50 {
		queue <- i
	}
	close(queue)
	wg.Done()
}

func consumer(i int, wg *sync.WaitGroup) {
	// for v := range queue {
	// 	fmt.Printf("消费内容为%d\n", v)
	// }
	for {
		v, ok := <-queue
		if !ok {
			fmt.Printf("消费者%d内容为%d消费完毕,结束消费\n", i, v)
			break
		}
		fmt.Printf("消费者%d消费内容为%d\n", i, v)

	}
	wg.Done()
}

```
