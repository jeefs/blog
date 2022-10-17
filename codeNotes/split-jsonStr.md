```
	jsonStr := "{k:v,k:v},{k:v,k:v}..." //get single {}
	var l, r int
	for i := 0; i < len(jsonStr); i++ {
		if jsonStr[i] == 123 {
			l = i
		}
		if jsonStr[i] == 125 {
			r = i
			str := jsonStr[l : r+1]
			fmt.Println(str)
		}
	}
```
