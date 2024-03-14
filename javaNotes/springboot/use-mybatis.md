### maven引入
```
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>3.0.3</version>
</dependency>
```

### springboot application.yaml配置数据源
```
server:
    port: 8088
spring:
    application:
        name: spring-boot-demo
    datasource:
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://127.0.0.1:3306/test?autoReconnect=true&useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Hongkong
        username: root
        password: 614168741
mybatis:
    mapper-locations: classpath:mapper/*.xml  #配置mapper.xml文件目录
```

### mapper.xml文件编写
``` xml
// resources/mapper/userDao.xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.mike.study.springbootdemo.dao.UserDao">
    <resultMap type="com.mike.study.springbootdemo.entity.User" id="userResult">
        <id column="id" property="id" />
        <result property="name" column="name"/>
        <result property="age" column="age"/>
    </resultMap>

    <select id="findAllUsers" resultMap="userResult">
        select * from user
    </select>


    <insert id="insertUser" parameterType="com.mike.study.springbootdemo.entity.User">
        insert into user(name,age) values(#{name},#{age})
    </insert>

    <update id="updateUser" parameterType="com.mike.study.springbootdemo.entity.User">
        update user set name=#{name},age=#{age} where id=#{id}
    </update>

    <delete id="deleteUser" parameterType="int">
        delete from user where id=#{id}
    </delete>

</mapper>
```
### mapper接口编写
```
// dao/UserDao.java
package com.mike.study.springbootdemo.dao;

import com.mike.study.springbootdemo.entity.User;

import java.util.List;

public interface UserDao {
    /**
     * 查询所有用户
     * @return
     */
    List<User> findAllUsers();

    /**
     * 添加用户
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 修改用户
     * @param user
     * @return
     */
    int updateUser(User user);

    /**
     * 删除用户
     * @param user
     * @return
     */
    int deleteUser(User user);


}

```
### 启动文件中配置容器注入扫描路径
```
package com.mike.study.springbootdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mike.study.springbootdemo.dao")  //自动注入UserDao
public class SpringBootDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }
}
```
### 实体
```
// entity/User.java
package com.mike.study.springbootdemo.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public class User {
    private Integer id;
    @NotEmpty
    private String name;

    @NotNull
    private Integer  age;

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

```

### 控制器调用
```
package com.mike.study.springbootdemo.controller;

import com.mike.study.springbootdemo.dao.UserDao;
import com.mike.study.springbootdemo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MybatisDemo {

    @Autowired
    private UserDao userDao;

    @GetMapping("/users/mybatis/queryAll")
    public List<User> findAllUsers() {
        return userDao.findAllUsers();
    }

    @PostMapping("/users/mybatis/insert")
    public Boolean insertUser(@RequestBody @Validated User user) {
        return userDao.insertUser(user) > 0;
    }

    @PostMapping("/users/mybatis/update")
    public Boolean updateUser(@RequestBody User user) {
        return userDao.updateUser(user) > 0;
    }

    @PostMapping("/users/mybatis/delete")
    public Boolean deleteUser(@RequestBody User user) {
        return userDao.deleteUser(user) > 0;
    }
}

```
