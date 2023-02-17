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
```
package main

import (
  "sync"
  "fmt"
)

func main() {
   var gData sync.Map
   chs :=  []chan int8{make(chan int8,1),make(chan int8,1)}
   for k,ch := range chs {
	go func(ch chan<- int8) {
		defer close(ch)
		index := k+1
		gData.Store(index,strct{})
		ch<-index
        }(ch)
   }
   
   
   
 
 
  
}
```
