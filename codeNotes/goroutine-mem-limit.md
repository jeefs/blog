### 限制协程资源分两部分
- 协程自身开销（栈内存）
- 协程处理的业务开销（堆内存）

### 限制方法
协程数量可以用有缓冲的通道限制，协程业务资源可以用信号量（semaphore）

单个任务

```golang
package main

import (
	"context"
	"fmt"
	"sync"
	"time"

	"golang.org/x/sync/semaphore"
)

// 模拟一个耗时的下游接口
func fetchData(ctx context.Context, id int) (string, error) {
	// 模拟随机耗时
	time.Sleep(time.Duration(id*100) * time.Millisecond)
	
	select {
	case <-ctx.Done(): // 核心能力：感知超时取消，立刻释放资源
		return "", ctx.Err()
	default:
		return fmt.Sprintf("结果-%d", id), nil
	}
}

func main() {
	// 1. 超时控制：总耗时不能超过 500ms
	ctx, cancel := context.WithTimeout(context.Background(), 500*time.Millisecond)
	defer cancel()

	// 2. 并发度限制：最多只能 3 个同时跑（防止压垮下游或内存溢出）
	sem := semaphore.NewWeighted(3) 
	
	var wg sync.WaitGroup
	results := make(chan string, 10) // 结果搜集

	for i := 1; i <= 10; i++ {
		wg.Add(1)
		
		go func(id int) {
			defer wg.Done()

			// 申请信号量（权重为 1）
			if err := sem.Acquire(ctx, 1); err != nil {
				return // 如果 context 已经超时，就不再启动新任务
			}
			defer sem.Release(1)

			// 执行抓取
			res, err := fetchData(ctx, id)
			if err != nil {
				fmt.Printf("任务 %d 失败或超时: %v\n", id, err)
				return // 3. 错误处理：一个失败不影响其他
			}
			
			results <- res
		}(i)
	}

	// 启动一个等待协程，确保所有任务完成后关闭通道
	go func() {
		wg.Wait()
		close(results)
	}()

	// 输出最终结果
	fmt.Println("开始收集成功的结果...")
	for r := range results {
		fmt.Println("成功获取:", r)
	}
}
```

多个任务块竞争
```golang
package main

import (
	"context"
	"fmt"
	"sync"
	"time"

	"golang.org/x/sync/semaphore"
)

func main() {
	// 1. 定义总资源：假设总共只有 5 个单位的资源（比如 5G 内存）
	sem := semaphore.NewWeighted(5)
	ctx := context.Background()
	var wg sync.WaitGroup

	// --- 模拟一批小任务 (每个占 1 权重) ---
	for i := 1; i <= 3; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			sem.Acquire(ctx, 1) // 领走 1 张票
			fmt.Printf("✅ 小任务 %d：领到 1 张票，开始干活...\n", id)
			time.Sleep(2 * time.Second)
			sem.Release(1)
			fmt.Printf("⬅️ 小任务 %d：干完归还了。\n", id)
		}(i)
	}

	// 稍微停一下，确保小任务先占住坑位
	time.Sleep(500 * time.Millisecond)

	// --- 模拟一个巨型任务 (需要 5 权重，也就是包场) ---
	wg.Add(1)
	go func() {
		defer wg.Done()
		fmt.Println("🚀 [巨型任务]：需要 5 张票，正在门口排队...")
		
		// 核心逻辑：这里会阻塞，直到前面所有小任务都还票，凑够 5 张
		if err := sem.Acquire(ctx, 5); err != nil {
			fmt.Printf("巨型任务失败: %v\n", err)
			return
		}
		
		fmt.Println("🔥 [巨型任务]：终于凑够 5 张票，正在全速运行 (包场中)...")
		time.Sleep(3 * time.Second)
		sem.Release(5)
		fmt.Println("✅ [巨型任务]：干完归还，大家可以进来了。")
	}()

	// --- 再来一个小任务，看看它是否要在巨型任务后面排队 ---
	time.Sleep(500 * time.Millisecond)
	wg.Add(1)
	go func() {
		defer wg.Done()
		fmt.Println("⌛ 新到的小任务：在门口等 1 张票...")
		sem.Acquire(ctx, 1)
		fmt.Println("✅ 新到的小任务：终于进来了。")
		sem.Release(1)
	}()

	wg.Wait()
	fmt.Println("所有任务结束。")
}
```
#### 代码基准测试法
func BenchmarkProcessData(b *testing.B) {
    for i := 0; i < b.N; i++ {
        // 调用你的业务函数
        yourBusinessLogic()
    }
}

#### 运行时监控法
```
var m1, m2 runtime.MemStats
runtime.ReadMemStats(&m1) // 记录执行前内存状态

doWork() // 执行具体的业务协程逻辑

runtime.ReadMemStats(&m2) // 记录执行后内存状态
fmt.Printf("业务耗费堆内存: %d KB\n", (m2.TotalAlloc - m1.TotalAlloc) / 1024)
```
一般不会写死权重，而是：根据输入参数动态计算： 比如一个处理图片的协程，权重 = 图片长 * 宽 * 4字节。设置安全水位线： 
利用 runtime.ReadMemStats 获取当前系统剩余内存。如果剩余内存低于 20%，则所有任务的 Acquire 权重翻倍，强行压低并发速度，防止 OOM
