### context链式取消子context
- 只会取消当前父节点下的子ctx

```
      rootCxt
         |
     /       \
parentCxt1   parentCxt2
    |             |
childCxt1    childCxt2

//main.go
package main

import (
    "context"
    "fmt"
)

func main() {
    package ctx

import (
	"context"
	"fmt"
)
func main() {
  CancelCxt() 
}

func CancelCxt() {
	rootCtx, rootCancelFunc := context.WithCancel(context.Background())
	done := [4]chan struct{}{
		make(chan struct{}),
		make(chan struct{}),
		make(chan struct{}),
		make(chan struct{}),
	}
	go func(cxt context.Context) {
		parentCxt, _ := context.WithCancel(cxt)
		go func(cxt context.Context) {
			select {
			case <-cxt.Done():
				fmt.Println("子协程1-1收到取消信号")
				done[0] <- struct{}{}
				return
			}
		}(parentCxt)

		select {
		case <-cxt.Done():
			fmt.Println("子协程1收到取消信号")
			done[1] <- struct{}{}
			return
		}
	}(rootCtx)

	go func(cxt context.Context) {
		parentCxt, _ := context.WithCancel(cxt)
		go func(cxt context.Context) {
			select {
			case <-cxt.Done():
				fmt.Println("子协程2-1收到取消信号")
				done[2] <- struct{}{}
				return
			}
		}(parentCxt)

		select {
		case <-cxt.Done():
			fmt.Println("子协程2收到取消信号")
			done[3] <- struct{}{}
			return
		}
	}(rootCtx)
	rootCancelFunc()
	<-done[0]
	<-done[1]
	<-done[2]
	<-done[3]
	draw := func() {
		fmt.Println(
			`      rootCxt` + "\n" +
				`         |` + "\n" +
				`     /       \ ` + "\n" +
				`parentCxt1   parentCxt2` + "\n" +
				`    |             |` + "\n" +
				`childCxt1    childCxt2` + "\n")
	}
	draw()
	fmt.Println("父协程退出")
}

```
