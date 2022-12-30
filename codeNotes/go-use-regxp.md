```
regexp_, err := regexp.Compile(`p([a-z]+)e`)
if err != nil {
    fmt.Println(err.Error())
}
match := regexp_.FindAllString(jsonStr, -1)
fmt.Println(match)



//校验手机号
mobile := "13145896969"
result, _ := regexp.MatchString(`^(1[3|4|5|6|7|8|9][0-9]\d{8})$`, mobile)
if !result {
    fmt.Println(fmt.Sprintf("手机号:%v非法", mobile))
}
```
