### CGO调用C

#### 1.直接调用代码

```
package main
/*
void hello() {
    char str[5] = "hello";
    printf("%s",str);
}
*/ 
import "C"
func main() {
    C.hello()
}

```

#### 2.调用头文件及库文件（头文件及源文件必须和mian.go文件在同一目录）
```
package main

/*
#include "hello.h"
*/
import "C"
func main() {
    C.hello()
}
```

#### 3.调用外部库（头文件及源文件文件和main.go不在一个目录）
需要先将头文件编译成静态库，然后引入
1. 编译生成中间文件
```
gcc -c hello.c -o hello.o
ar -crs libhello.a hello.o
```

2.在main.go中引入静态库(假设hello.c和hello.h在lib目录下，且lib目录和mian.go同一层级)
```
package main

/*
#include <stdio.h>
#include <stdlib.h>
#cgo CFLAGS: -I./lib //指定头文件目录 
#cgo LDFLAGS: -L./lib -lhello -L./Lib2 -l2 //指定要链接的静态库目录，及库名称，注意，虽然静态库名称为libhello.a 
但是在-l参数后只需要指定 hello即可，前缀lib和后缀.a可以省略,如果hello里面还依赖了其他头文件，需要重复步骤1编译成静态库，并在此引入
*/
```
#### 4.编译go
当引入了C头文件后，不能使用go run 来编译，需使用go build . 会自动将C头文件包含进来

#### 5.cgo和go一些基础类型转换
参考 1.https://chai2010.cn/advanced-go-programming-book/ch2-cgo/ch2-03-cgo-types.html

```  int
#### go to c
var i int
ci := C.int(i)

#### c to go
var i C.int
goi := int(i)
```

``` go float32 to c float *p1指针
 c函数定义： int foo(float *p1);
 go调用
 var res float32
 C.foo((*C.float)(unsafe.Pointer(&res))
```

``` string
#### go to c
var str string
cstr := C.CString(str)

#### c to go
/*
#include <stdlib.h>
#include <stdio.h>
char foo[] = "hellofoo";
char *bar = "hellobar";
*/
import "C"
import "fmt"

func main() {
    fmt.Printf("%s\n", C.GoString(&C.foo[0]))
    fmt.Printf("%s\n", C.GoString(C.bar))
}
```

``` arry
#### c to go 
/*
#include <stdio.h>
int cIArray[] = {1, 2, 3, 4, 5, 6, 7};
float cFArray[] = {1.011, 2.022, 3.022, 4.023, 5.02, 6.03, 7.045};
*/
import "C"
import "fmt"

func main() {
    goIArray := C.cIArray[:]
    fmt.Println(goIArray)
    goFArray := C.cFArray[:]
    fmt.Println(goFArray)
}

```
