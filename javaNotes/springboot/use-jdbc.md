### 配置
maven pom.xml添加依赖包
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>

<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.49</version>
</dependency>
```

application.yaml配置
```
server:
    port: 8088
spring:
    application:
        name: spring-boot-demo
    datasource:
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://127.0.0.1:3306/test?useSSL=false
        username: root
        password: xxxxx
```

### jdbc增删改查

```  JdbcDemo.java
package com.mike.study.springbootdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class JdbcDemo {
    @Autowired
    JdbcTemplate jdbcTemplate;

    //增加一条用户记录
    @PostMapping("/users/insert")
    public Object insert(@RequestBody @Validated  User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String,String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(),error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        String name = user.getName();
        Integer age = user .getAge();
        System.out.println(name);
        String sql = "INSERT INTO user(name,age) VALUES(?,?)";
        try {
            jdbcTemplate.update(sql,name,age);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inserting user");
        }
    }

    //查询所有用户记录
    @GetMapping("/users/queryAll")
    public List<Map<String,Object>> queryAll() {
        List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT * FROM user");
        return list;
    }

    //修改用户记录
    @PostMapping("/users/update")
    public Object update(@RequestBody JdbcVo vo) {
        List<String> condition = new ArrayList<>();
        List<Object> args = new ArrayList<>();
        //Object[] args2 = new Object[]{"mike",5};
        String sql = "UPDATE user SET ";
        Integer id = vo.getId();
        if (id == null) {
            System.out.println("更新条件为空!");
            return false;
        }
        String name = vo.getName();
        Integer age = vo.getAge();
        if (name != null && !name.isEmpty()) {
            condition.add(" name = ? ");
            args.add(name);
        }
        if (name != null && !(age == null)) {
            condition.add(" age = ? ");
            args.add(age);
        }
        args.add(id);
        //对map或者list调用isEmpty方法，当值为null时会报NonPointerException
        if (condition.isEmpty()) {
            System.out.println("更新值为空!");
            return false;
        }
        sql += String.join(",",condition) + " WHERE id = ? ";
        System.out.println("sql:" + sql + " args:" + args);
        try {
            jdbcTemplate.update(sql,args.toArray());
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    //删除用户记录
    @GetMapping("/users/delete")  //TODO 失败原因分析
    public Object update(@RequestParam("id") Integer id) {
        try {
            jdbcTemplate.update("DELETE FROM user WHERE id = ?",id);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

```

``` User.java
package com.mike.study.springbootdemo.controller;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public class User {
    @NotEmpty
    private String name;

    @NotNull
    private Integer  age;

    public String getName() {
        return this.name;
    }

    public Integer getAge() {
        return this.age;
    }
}
```

``` JdbcVo.java
package com.mike.study.springbootdemo.controller;

public class JdbcVo {
    private Integer id;

    private String name;

    private Integer age;

    public Integer getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }

    public Integer getAge() {
        return this.age;
    }
}

```
