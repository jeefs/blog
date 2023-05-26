### 安装
go get github.com/xuri/excelize/v2


### 使用
```
package main

import (
	"fmt"
	"github.com/xuri/excelize/v2"
	"strings"
)

type User struct {
	Phone  string `json:"phone"`
	Name   string `json:"name"`
	Age    int    `json:"age"`
	Gender int    `json:"gender"`
}

func main() {
	userInfo := []User{
		{Phone: "18866666666", Name: "mike", Age: 18, Gender: 1},
	}
	filePath := "test.xlsx"
	_, err := writeXlsx(userInfo, filePath)
	if err != nil {
		fmt.Printf("写入excle错误:%v", err)
	}
}

func writeXlsx(list []User, savePath string) (string, error) {
	defer func() {
		if err := recover(); err != nil {
			fmt.Println(err)
		}
	}()
	sheetTitle := []interface{}{"姓名", "手机号码", "年龄", "性别"}
	sheetName := strings.TrimSuffix(savePath, ".xlsx")
	file := excelize.NewFile()
	_, err := file.NewSheet(sheetName)
	if err != nil {
		panic(fmt.Sprintf("创建工作表错误:%v", err))
	}
	streamWriter, err := file.NewStreamWriter(sheetName)
	if err != nil {
		panic(fmt.Sprintf("初始化流错误:%v", err))
	}
	err = streamWriter.SetRow("A1", sheetTitle) //设置表头
	if err != nil {
		panic(fmt.Sprintf("写入表头错误:%v", err))
	}
	for rowID := 0; rowID < len(list); rowID++ { //行数
		row := make([]interface{}, 4) //列数
		detail := list[rowID]         //行数据
		row[0] = detail.Name
		row[1] = detail.Phone
		row[2] = detail.Age
		row[3] = detail.Gender
		cell, _ := excelize.CoordinatesToCellName(1, rowID+2)
		if err = streamWriter.SetRow(cell, row); err != nil {
			panic(fmt.Sprintf("写入行错误:%v", err))
		}
	}
	if err = streamWriter.Flush(); err != nil {
		panic(fmt.Sprintf("缓冲区写入磁盘错误:%v", err))
	}
	err = file.DeleteSheet("Sheet1")
	if err != nil {
		panic(fmt.Sprintf("删除sheet1错误:%v", err))
	}
	if err = file.SaveAs(savePath); err != nil {
		panic(fmt.Sprintf("生成文件错误:%v", err))
	}
	err = file.Close()
	if err != nil {
		panic(fmt.Sprintf("关闭文件流错误:%v", err))
	}
	return savePath, nil
}


```
[源码地址](../project/excelize-demo)


