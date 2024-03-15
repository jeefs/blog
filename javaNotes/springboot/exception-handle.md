### 异常捕获注解
- @RestControllerAdvice  springboot提供的请求拦截器注解，可以对请求或响应做全局拦截并处理
- @ExceptionHandler      springboot提供的控制器全局异常捕获注解

### 自定义统一返回对象，开启全局异常处理

1.统一返回值对象
```java
//ResultData.java
package com.mike.study.springbootdemo.common;

import lombok.Data;


@Data
public class ResultData<T> {
    /** 结果状态 ,具体状态码参见ResultData.java*/
    private int status;
    private String message;
    private T data;
    private long timestamp ;


    public ResultData (){
        this.timestamp = System.currentTimeMillis();
    }


    public static <T> ResultData<T> success(T data) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setStatus(ReturnCode.RC100.getCode());
        resultData.setMessage(ReturnCode.RC100.getMessage());
        resultData.setData(data);
        return resultData;
    }

    public static <T> ResultData<T> fail(int code, String message) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setStatus(code);
        resultData.setMessage(message);
        return resultData;
    }
}
```

2.统一错误码
```java
//ReturnCode.java
package com.mike.study.springbootdemo.common;

public enum ReturnCode {
    /**
     * 操作成功
     **/
    RC100(100, "操作成功"),
    /**
     * 操作失败
     **/
    RC999(999, "操作失败"),
    /**
     * 服务限流
     **/
    RC200(200, "服务开启限流保护,请稍后再试!"),
    /**
     * 服务降级
     **/
    RC201(201, "服务开启降级保护,请稍后再试!"),
    /**
     * 热点参数限流
     **/
    RC202(202, "热点参数限流,请稍后再试!"),
    /**
     * 系统规则不满足
     **/
    RC203(203, "系统规则不满足要求,请稍后再试!"),
    /**
     * 授权规则不通过
     **/
    RC204(204, "授权规则不通过,请稍后再试!"),
    /**
     * access_denied
     **/
    RC403(403, "无访问权限,请联系管理员授予权限"),
    /**
     * access_denied
     **/
    RC401(401, "匿名用户访问无权限资源时的异常"),
    /**
     * 服务异常
     **/
    RC500(500, "系统异常，请稍后重试"),

    INVALID_TOKEN(2001, "访问令牌不合法"),
    ACCESS_DENIED(2003, "没有权限访问该资源"),
    CLIENT_AUTHENTICATION_FAILED(1001, "客户端认证失败"),
    USERNAME_OR_PASSWORD_ERROR(1002, "用户名或密码错误"),
    UNSUPPORTED_GRANT_TYPE(1003, "不支持的认证模式"),

    PARAMETER_ERROR(1004,"请求参数异常");




    /**自定义状态码**/
    private final int code;
    /**自定义描述**/
    private final String message;

    ReturnCode(int code, String message){
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
```

3.开启全局请求处理
```java
//ResponseAdvice.java
package com.mike.study.springbootdemo.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//controller拦截器，统一处理返回值，可用于加密，签名
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(body instanceof String){
            return objectMapper.writeValueAsString(ResultData.success(body));
        }
        if(body instanceof ResultData){
            return body;
        }
        return ResultData.success(body);
    }
}

```

4.全局处理各种异常
```java
//RestExceptionHandler.java
package com.mike.study.springbootdemo.common;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Map;


//@Slf4j  TODO 暂未接入日志系统

//全局异常处理器,用户校验失败时自动捕获异常信息
@RestControllerAdvice
public class RestExceptionHandler {
    /**
     * 处理方法的普通参数异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResultData<Map<String, String>> exception(ConstraintViolationException e) {
        //Map<String, String> errorMap = new HashMap<>();
        String errorMapStr = "";
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            String field = "";
            for (Path.Node node : constraintViolation.getPropertyPath()) {
                field = node.getName();
            }
            //errorMap.put(field, constraintViolation.getMessage());
            errorMapStr += field + ":" + constraintViolation.getMessage()  + ",";
        }
        return ResultData.fail(ReturnCode.PARAMETER_ERROR.getCode(),errorMapStr);
    }

    /**
     * 处理方法的实体参数异常
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResultData<Map<String, String>> exception(BindException e) {
        //Map<String, String> errorMap = new HashMap<>();
        //e.getBindingResult().getFieldErrors().forEach(r -> errorMap.put(r.getField(), r.getDefaultMessage()));
        String errorMapStr = "";
        for (FieldError r: e.getBindingResult().getFieldErrors()) {
            errorMapStr += r.getField() + ":" + r.getDefaultMessage()  + ",";
        }
        return ResultData.fail(ReturnCode.PARAMETER_ERROR.getCode(), errorMapStr.trim());
    }
    /**
     * 处理方法的参数格式异常
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResultData<String> exception(HttpMessageNotReadableException e) {
        return ResultData.fail(ReturnCode.PARAMETER_ERROR.getCode(), e.getMessage());
    }



    /**
     * 处理方法的参数缺失
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResultData<String> exception(MissingServletRequestPartException e) {
        return ResultData.fail(ReturnCode.PARAMETER_ERROR.getCode(), e.getMessage());
    }


    /**
     * 处理方法的参数缺失
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResultData<String> exception(MissingServletRequestParameterException e) {
        return ResultData.fail(ReturnCode.PARAMETER_ERROR.getCode(), e.getMessage());
    }

    /**
     * 处理方法的参数类型不匹配异常
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResultData<String> exception(MethodArgumentTypeMismatchException e) {
        return ResultData.fail(ReturnCode.PARAMETER_ERROR.getCode(), e.getMessage());
    }
    /**
     * 处理方法的参数类型转换异常
     */

    @ExceptionHandler(value = MethodArgumentConversionNotSupportedException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResultData<String> exception(MethodArgumentConversionNotSupportedException e) {
        return ResultData.fail(ReturnCode.PARAMETER_ERROR.getCode(), e.getMessage());
    }



    /**
     * 全局异常处理。
     * @param e the e
     * @return ResultData
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultData<String> exception(Exception e) {
        //log.error("全局异常信息 ex={}", e.getMessage(), e);
        System.out.println("全局异常信息 ex=" +  e.getMessage());
        return ResultData.fail(ReturnCode.RC500.getCode(),e.getMessage());
    }
}

```
