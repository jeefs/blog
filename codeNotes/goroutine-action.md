### 协程几种常用模式

1.主协程等待子协程执行完毕后再退出

``` go
package main

import (
	"fmt"
	"sync"
)

func main() {

	defer func() {
		fmt.Println("主协执行完毕")
	}()
	var wg sync.WaitGroup
	for i := 1; i < 10; i++ {
		wg.Add(1)
		i := i
		go func() {
			i := i
			fmt.Printf("子协程%v,开始执行\n", i)
			wg.Done()
			fmt.Printf("子协程%v,执行完毕\n", i)
		}()
	}
	wg.Wait()
	fmt.Println("所有子协程执行完毕")
}

```

2.多个子协程读写变量
一般情况下，多个协程共享读写数据会出现数据竞争，为了避免这种情况可以使用sync.Map来保证线程安全
``` go
package main

import (
	"fmt"
	"sync"
)

func main() {
	defer func() {
		fmt.Println("主协程执行完毕\n")
	}()
	var gData sync.Map
	chs := []chan int{make(chan int, 1), make(chan int, 1)}
	for k, ch := range chs {
		index := k + 1
		go func(ch chan<- int) {
			fmt.Printf("子协程%v开始执行\n", index)
			defer close(ch)
			gData.Store(index, struct{}{}) //sync.Map写入必须使用Store(k,v)方法
			ch <- index
			fmt.Printf("子协程%v执行完毕\n", index)
		}(ch)
	}
	<-chs[0] //利用有缓冲的通道实现主协程等待子协程
	<-chs[1]
	fmt.Println("所有子协程执行完毕\n")
	gData.Range(func(key, value any) bool {  //sync.Map读取必须使用Range()方法
		fmt.Printf("数据key：%v,val:%v\n", key, value)
		return true
	})
}

```
