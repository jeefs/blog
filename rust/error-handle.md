### rust中错误处理使用match模式匹配，配合Option,Result等枚举类型来完成，还有unwarp可以实现隐式匹配

```
fn main() {
  //let s = "panic";
  //panic_case("panic");
  exp::errors::unwarp_case(Some("tom"));

  let result = exp::errors::match_case2(String::from("no"));
  match result {
      Ok(v) => {println!("{}",v)},
      Err(e) => {println!("{}",e)},
  }

}

// match 会对比指定值与条件，如果匹配则执行对应分支
#[allow(dead_code)]
fn match_case() {
  let number = 2;
    match number {
        2 => println!("匹配成功值为{}",number),
        _ => println!("没有匹配到任何值"),
    }
}

pub fn match_case2(s:String) -> Result<String,std::io::Error> {
  if s == "ok" {
    Ok(s)
  } else {
    Err(std::io::Error::new(std::io::ErrorKind::Other, "err"))
  }
}

#[allow(dead_code)]
pub fn panic_case(s:&'static str) {
   if s == "panic" {
       panic!("panic,now!");
   } else {
       println!("everthig is be ok!");
   }
}

//unwarp如何匹配到Some(T)则返回T的值,如果匹配到None则直接panic
pub fn unwarp_case(s:Option<&str>) {
  let result = s.unwrap();
  if result == "mike" {
    println!("my name is {}",result);
  } else if result == "tom" {
    println!("my name is {}",result);
  }
}

//TODO ?错误传播
```
