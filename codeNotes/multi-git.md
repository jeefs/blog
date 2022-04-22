### 目录
- [1.需求分析](#1)
- [2.ssh-key配置](#2)
- [3.git配置](#3)
- [4.多账号切换](#4)



### 正文
<h3 id="1">1.需求分析</h3>
	大多数情况下，工作环境只会配置一个git账号，即大家都在一个项目下开发，推送代码也是推到一个仓库的不同分支。由于平时会将一些开发笔记同步到git仓库，所以需要能在一个环境变量下使用能自由的切换两个git号，自己个人账号用户记录笔记，公司git账号用户用于项目,解决这个问题的办法有好集中，本次采用ssh config配置来实现
	

<h3 id="2">2.ssh-key配置</h3>
1.`cd ~/.ssh` 在本地 .ssh目录下创建一个config文件 `touch config`
2.`vim config` 打开文件添加如下内容:

```
//账号a
Host          ssh-work    //配置别名
HostName      xxx.xxx.com //仓库的域名，如果是自建的每个公司不一样
User          git         //用户名，一般情况下为git
IdentityFile  C:\Users\mike\.ssh\id_rsa_work //私匙文件路径，此目录每个环境不相同,一般在用户下的.ssh文件夹

//账号b
Host          ssh-person //配置别名
HostName      github.com //仓库的域名，个人使用git仓库
User          git        //用户名，一般情况下为git
IdentityFile  C:\Users\mike\.ssh\id_rsa_person // 私钥文件路径，此目录每个环境不相同，一般在用户下的.ssh文件夹                                                                                                      
```

3.在.ssh目录下分别创建2个ssh key 执行`ssh-keygen -t rsa -C "your_email@example.com"`  邮箱地址为你的git账号,在`Enter file in which to save the key(/your_home_path/.ssh/id_rsa):`这一步输入id_rsa_work,后面步骤可以按回车默认，id_rsa_person key和id_rsa_work生成方法一致

4.添加key到ssh 环境变量执行
`ssh-add ~/.ssh/id_rsa_person 和ssh-add ~/.ssh/id_rsa_work`


<h3 id="3">3.git配置</h3>
1.取消git全局配置：`执行 git config --global --unset user.name`
`执行git config --global --unset user.email`

2.将ssh公匙配置到git账号中，打开github设置->选择SSH and GPG keys -> New  key 将 id_rsa_person 中的内容全部复制到 Key的文本框中，Title可以随便取，保存即可，
如果时gitlab账户则打开设置->SSH keys 将 id_rsa_work目录复制到文本框中，保存即可

3.5.测试配置
执行 `ssg -T git@ssh-work `  返回welcome信息则配置成功

<h3 id="4">4.多账号切换</h3>
首先在需要使用个人git账户的目录，配置git config --global user.name "your_username"，git config --global user.email "your_registered_github_Email" ，此配置为局部配置

在需要使用公司git账户的目录，配置git config --global user.name "your_username"，git config --global user.email "your_registered_github_Email" ，此配置为局部配置

1.拉取个人仓库项目，从仓库中复制链接 如 git@github.com:jeefs/blog.git 
替换成   `git clone git@ssh-person:jeefs/blog.git`
同理，拉取公司项目，从仓库复制链接 ，如git@xxxx.xxxx.com:jeefs/blog.git 
替换成   `git clone git@ssh-work:jeefs/blog.git`
通过上述配置，可以在不同的目录使用不同的git账号，他们彼此之间隔离

也可以关联本地已经存在的项目
git remote add origin git@ssh-work:用户名/项目名.git 
git remote add origin git@ssh-person:用户名/项目名.git
```
