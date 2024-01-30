### 使用sync.Mutex互斥锁的注意事项：

- 不要重复锁定互斥锁；
- 不要忘记解锁互斥锁，必要时使用defer语句；
- 不要对尚未锁定或者已解锁的互斥锁解锁；
- 不要在多个函数之间直接传递互斥锁。

### 对于同一个sync.RWmutex读写锁来说有如下规则:
- 在写锁已被锁定的情况下再试图锁定写锁，会阻塞当前的goroutine。
- 在写锁已被锁定的情况下试图锁定读锁，会阻塞当前的goroutine。
- 在读锁已被锁定的情况下试图锁定写锁，会阻塞当前的goroutine。
- 在读锁已被锁定的情况下再试图锁定读锁，不会阻塞当前的goroutine。

### 利用sync.Mutex互斥锁保证变量能被多个协程正常读写
```
package main

import (
	"fmt"
	"sync"
	"time"
)
type Counter struct {
	num uint         // 计数。
	mu  sync.RWMutex // 读写锁。
}

// number 会返回当前的计数。
func (c *Counter) number() uint {
	c.mu.RLock()
	defer c.mu.RUnlock()
	return c.num
}

// add 会增加计数器的值，并会返回增加后的计数。
func (c *Counter) add(increment uint) uint {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.num += increment
	return c.num
}

func main() {
	c := &Counter{}
	// sign 用于传递演示完成的信号。
	sign := make(chan struct{}, 3)
	go func() { // 用于增加计数。
		defer func() {
			sign <- struct{}{}
		}()
		c.add(1)
		fmt.Println("计数add执行")
		time.Sleep(1 * time.Second)
	}()
	go func() {
		defer func() {
			sign <- struct{}{}
		}()
		c.add(1)
		fmt.Println("计数add执行")
		time.Sleep(1 * time.Second)
	}()
	go func() {
		defer func() {
			sign <- struct{}{}
		}()
		time.Sleep(2 * time.Second)
		fmt.Printf("计数器和为%v\n", c.number())
	}()
	<-sign
	<-sign
	<-sign
}
```

### 利用sync.Cond实现多个子协程等待主线程
```
package main

import (
        "fmt"
        "sync"
        "time"
)

func Read(done *bool, cond *sync.Cond, wg *sync.WaitGroup) {
	cond.L.Lock()
	for !*done {
		cond.Wait()
	}
	time.Sleep(1 * time.Second)
	cond.L.Unlock()
	fmt.Println("start read")
	wg.Done()
}

func Write(done *bool, cond *sync.Cond, wg *sync.WaitGroup) {
	cond.L.Lock()
	*done = true
	cond.L.Unlock()
	fmt.Println("start write")
	cond.Broadcast()
	wg.Done()
}

func main() {
    var wg sync.WaitGroup
    wg.Add(2)
    var done bool
    cond := sync.NewCond(&sync.Mutex{})
    go mutex.Read(&done, cond, &wg)
    go mutex.Read(&done, cond, &wg)
    mutex.Write(&done, cond, &wg)
    wg.Wait()
    fmt.Println("finish")
}
```
