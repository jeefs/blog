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
