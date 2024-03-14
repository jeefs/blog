### 添加依赖
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### 实体对象校验
1.在需要校验的实体类属性标注规则注解
```
public class User {

    @NotNull(message = "用户id不能为空!")
    private Integer id;
    @NotEmpty(message = "用户名称不能为空!")
    private String name;

    @NotNull(message = "用户年龄不能为空!")
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
2.在方法参数标注@Validated
```
@RestController
public class MybatisDemo {

    @Autowired
    private UserDao userDao;

    @PostMapping("/users/mybatis/insert")
    public Boolean insertUser(@RequestBody @Validated User user) {
        return userDao.insertUser(user) > 0;
    }

    @PostMapping("/users/mybatis/delete")
    public Boolean deleteUser(@RequestBody @Validated User user) {
        return userDao.deleteUser(user) > 0;
    }
}

```
### 普通参数校验
直接在方法参数标注规则注解，并在控制器类名标注@Validated
```
@RestController
@Validated
public class JdbcDemo {
    @GetMapping("/users/delete")
    public Object delete(@RequestParam("id") @Positive(message = "id必须大于0!") Integer id) {
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

### 分组校验
分组校验用于不同情景下启用不同的验证规则，例如在新增用户和删除用户逻辑中都使用实体对象参数，但这两个场景需要校验的字段不一样

1.定义分组接口
```
//删除用户分组
public interface DeleteUserValidGroup {

}

```

```
//新增用户分组
public interface InsertUserValidGroup {

}
```
2.实体对象属性指定分组名为接口类名
```
public class User {

    @NotNull(groups = {UpdateUserValidGroup.class, DeleteUserValidGroup.class},message = "用户id不能为空!")
    private Integer id;
    @NotEmpty(groups = InsertUserValidGroup.class,message = "用户名称不能为空!")
    private String name;

    @NotNull(groups = InsertUserValidGroup.class,message = "用户年龄不能为空!")
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
3.在方法参数申明注解并指定分组名称
```
    @PostMapping("/users/mybatis/insert")
    public Boolean insertUser(@RequestBody @Validated(InsertUserValidGroup.class) User user) {
        return userDao.insertUser(user) > 0;
    }

    @PostMapping("/users/mybatis/delete")
    public Boolean deleteUser(@RequestBody @Validated(DeleteUserValidGroup.class) User user) {
        return userDao.deleteUser(user) > 0;
    }
```

单纯的使用校验规则会有一些问题，例如在校验失败时，框架会直接抛出异常，可以采用局部异常处理和全局异常处理来避免
