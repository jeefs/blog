### enum 关键字允许创建一个从数个不同取值中选其一的类型
```
enum Ball {
    Football,
    Rugby,
    Basketball,
}

//枚举类型必须在match分支处理每一个枚举值
fn match_enum_case(ball:Ball){
    match ball {
        Ball::Basketball => {
           println!("basketball");
        }
        Ball::Football => {
           println!("football");
        }
        Ball::Rugby => {
           println!("rugby");
        }
    }
}


let basketball = Ball::Basketball; //输出basketball
match_enum_case(basketball);
```




Option枚举,用来匹配有值或无值的情况
```
fn match_enum(s:Option<&str>) {
  match s {
      Some(v) => println!("{}",v),
      None => println!("值为None"),
  }
}
let ball:Option<&str> = None; //输出值为None
let ball:Option<&str> = Some("hello");//输出hello
match_enum(ball);

```

Result枚举，用来匹配值和错误的情况
```
fn match_enum(s:Option<&str>) -> Result<&str,&str> {
  match s {
       Some(v) => {
           let vv:Result<&str,&str> = Ok(v);
           vv
       },
       None => {
          let e:Result<&str,&str> = Err("参数不能为空");
          e
       },
  }
}

fn main() {
    let param:Option<&str> = Some("hello");//输出hello
    let param:Option<&str> = None; //输出参数不能为空
    let pure_result = match match_enum2(param) {
      Ok(v) => v,
      Err(e) => e, 
    };
    println!("{}",pure_result);
}
```
