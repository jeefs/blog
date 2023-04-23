### 安装
```
go get github.com/go-swagger/go-swagger  //下载go-swagger包

go install ./cmd/swagger //编译并安装执行文件到gobin
```

### gin框架引入
```
main.go文件引入

import(
    "_ "cgm_manager/cmd/admin/docs" // 这里需要引入本地已生成文档"
    swaggerFiles "github.com/swaggo/files"
    ginSwagger "github.com/swaggo/gin-swagger"
)

ginRouter := gin.Default()
ginRouter.GET("/cmg_manager/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))
```


### 编写文档
```
// AddUser
// @tags 接口分组
// @Summary 接口名称
// @Accept application/json
// @Produce application/json
// @Param param body resp.Req true "参数"
// @Security ApiKeyAuth
// @router /api/addUser [POST]
// @success 200 {object} responses.ResponseSuc
func AddUser(c *gin.C) {
//todo logic code
c.JSON(http.StatusOK, responses.ResponseSuc{
		200,
		"ok",
	})
	return 
}
```

### 生成文档
创建两个shell文件，用于生成不同目录下的文档
```
#!/bin/bash
swag init -g cmd/admin/main.go -o cmd/admin/docs

#!/bin/bash
swag init -g cmd/dataReport/main.go -o cmd/dataReport/docs
```
