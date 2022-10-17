```
regexp_, err := regexp.Compile(`p([a-z]+)e`)
if err != nil {
    fmt.Println(err.Error())
}
match := regexp_.FindAllString(jsonStr, -1)
fmt.Println(match)
```
