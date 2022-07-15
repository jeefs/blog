### 为gitlab生成一对秘钥ssh key
```
ssh-keygen -t rsa -C 'yourEmail@xx.com' -f ~/.ssh/gitlab-rsa
```

### 为github生成一对秘钥ssh key
```
ssh-keygen -t rsa -C 'yourEmail2@xx.com' -f ~/.ssh/github-rsa
```

### 在~/.ssh目录下新建名称为config的文件（无后缀名）。用于配置多个不同的host使用不同的ssh key，内容如下：
```
# gitlab
Host gitlab.com
    HostName gitlab.com
    PreferredAuthentications publickey
    IdentityFile ~/.ssh/gitlab-rsa
# github
Host github.com
    HostName github.com
    PreferredAuthentications publickey
    IdentityFile ~/.ssh/github-rsa
# 配置文件参数
# Host : Host可以看作是一个你要识别的模式，对识别的模式，进行配置对应的的主机名和ssh文件
# HostName : 要登录主机的主机名
# User : 登录名
# IdentityFile : 指明上面User对应的identityFile路径
```

### 测试连接
```
ssh -T git@gitlab.com
ssh -T git@github.com
```

### 克隆项目
```
git clone git@github.com:jeefs/blog.git
```
