### rust中错误处理使用match模式匹配，配合Option,Result等枚举类型来完成，还有unwarp可以实现隐式匹配


### match匹配
match类似其他语言中的switch，当待匹配值与条件中值相等，则会执行相应的条件分支
```
    let say = "hello";
    match say {//当say的值为hello时，会输出hello,当say为其他值时输出missmatch,_类似其他语言中default,未匹配条件时会执行
       "hello" => println!("{}",say),
       _ => println!("missmatch"),
    }
    
```

### !panic()
!panic宏被执行时，程序会停止运行，一般发生不可恢复的错误时使用


### Option<Some(T),None> 枚举类型
Option表示一个包含值或不包含值的情况，它的枚举成员Some(T),表示包含值，None表示不包含值
```
let say = Some("hello");
match say {//当say的值为Some("hello")时输出hello,当say的值为None时输出missmatch
    Some(v) => println!("{}",v),
    None => println!("missmatch"),
}
```

### Result<OK(T),Err(E)> 枚举类型
Result表示成功时返回值，失败返回错误
```
  let say:Result<&str,&str> = Ok("hello");
   
  //let say:Result<&str,&str> = Err("err");


  let vv = match say {
    Ok(v) => v,
    Err(e) => panic!("{}",e)
  };
  println!("{}",vv); 
```

### unwarp 错误隐式处理
unwrap会自动匹配Option类型，如果是Some(T)即有值，则返回值，如果是None会直接panic
```
    //let say:Option<&str> = None; //当say的值为None时，unwrap()会直接panic,当有值时会返回hello
    let say:Option<&str> = Some("hello");
    println!("{}",say.unwrap());
```

//TODO ?错误传播
