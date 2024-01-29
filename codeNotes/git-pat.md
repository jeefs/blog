### 报错信息
```
remote: Support for password authentication was removed on August 13, 2021. Please use a personal access token instead
```
上述报错是git access token过期，需要重新生成


### 生成token
1，打开Github，在个人设置页面，找到【Setting】，然后打开找到【Devloper Settting】，如下图。
![](https://ask.qcloudimg.com/http-save/yehe-1148531/881ce2b5e9070dc10db585f50eabca11.png)

然后，选择个人访问令牌【Personal access tokens】，然后选中生成令牌【Generate new token】。
![](https://ask.qcloudimg.com/http-save/yehe-1148531/eee4312222dd36ce36bc4e9ff9b120a2.jpg)

在上个步骤中，选择要授予此令牌token的范围或权限。
- 要使用token从命令行访问仓库，请选择repo
- 要使用token从命令行删除仓库，请选择delete_repo
- 其他根据需要进行勾选

其他根据需要进行勾选
![](https://ask.qcloudimg.com/http-save/yehe-1148531/debd2b455b97c4cc83af104dde80c3ee.png)

![](https://ask.qcloudimg.com/http-save/yehe-1148531/aafe782054bea8007b896adad0829fc6.png)
生成token后，记得把你的token保存下来，以便进行后面的操作。把token直接添加远程仓库链接中，这样就可以避免同一个仓库每次提交代码都要输入token了
```
git remote set-url origin https://<your_token>@github.com/<USERNAME>/<REPO>.git
```
例如: git remote set-url origin https://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@github.com/shliang0603/Yolov4_DeepSocial.git/
