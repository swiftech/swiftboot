# SwiftBoot-Web 高级



### 子集合的参数自动填充

  对于编辑一个对象时的子集合的增删改查，无需对子对象分别进行各自的处理，只需要查询父实体并对其子实体集合执行 `clear()` 操作， 然后再调用 `populateEntity()` 将参数中的子集合填充至父实体中，SwiftBoot 会自动判断实体是否存在，如果已经存在则进行更新，如果不存在则新增，其余不在子集合中的实体都会被删除。
  例如：


父参数对象定义，包含子对象的集合：
  ```java
  public class OrderSaveRequest extends BasePopulateRequest<OrderEntity> {
    private List<OrderDetail> details;
  }
  ```

子参数对象定义：
  ```java
  public class OrderDetail extends BasePopulateRequest<OrderDetailEntity> {
  }
  ```

实体类定义子实体类集合：
  ```java
  @Entity
  @Table(name = "DEMO_ORDER")
  public class OrderEntity extends BaseEntity {
      @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order", orphanRemoval = true)
      private List<OrderDetailEntity> details;
  }
  ```

实现业务逻辑：
  ```java
  public void editOrder(OrderSaveRequest request) {
    Optional<OrderEntity> optEntity = orderRepository.findById(request.getId());
    if (optEntity.isPresent()) {
        OrderEntity orderEntity = optEntity.get();
        orderEntity.getDetails.clear(); // 清除原有子实体集合
        request.populateEntity(orderEntity); // 填充实体，包括子集合中的编辑、新增的对象
        orderRepository.save(orderEntity);
    }
  }
  ```

以上代码实现简化了子集合的操作：
* 无需直接操作子集合中的对象就能自动填充子实体。
* 无需区别处理集合中删除、编辑和新增的子对象。


### 错误处理

构造 `Response` 可以将错误代码传入，`ResponseCode` 这个类提供了一些常见的错误代码（从 3000 开始）。例如：

```java
return new Response(ResponseCode.CODE_PARAMS_ERROR); // 返回 '输入参数错误' 给客户端
```

```json
{
  "code": "4001",
  "message": "输入参数错误"
}
```

> 如果需要改变错误信息输出，那么可以传入第二个参数： `new Response(ResponseCode.CODE_PARAMS_ERROR, "我的错误信息");`，
> 如果资源存在参数化：`new ResponseBuilder<Void>().code(ResponseCode.CODE_PARAMS_ERROR).msg("我的错误信息: {0} {1}").msgParams("参数0", "参数1"")";`
> `ResponseBuilder` 也可以创建非参数化的 `Response`

如果需要自定义错误代码，那么实现一个 Bean，将错误代码定义以静态字符串变量放入这个 Bean 中，变量名需要以 `CODE_` 开头，并在启动时进行加载，例如：

```java
@Component
public class ErrorCode {

    public static final String CODE_MY_ERR_CODE1 = "4000";
    public static final String CODE_MY_ERR_CODE2 = "4001";

    @Resource
    private ResponseCode errorCodeSupport;

    @PostConstruct
    public void init() {
        try {
            errorCodeSupport.loadFromClass(this.getClass());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
```

> 为了不和默认的 code 冲突，自定义的错误代码取值从4000开始


### 统一 API 异常处理

  控制器中抛出的异常直接抛出会使得客户端的错误处理非常不友好，而通过代码去捕获即繁琐又容易遗留，SwiftBoot 实现了控制器增强 `ExceptionProcessor`，他将异常信息以统一的 `JSON` 格式输出给客户端，配置方法如下：


  ```java
  @Configuration
  @EnableWebMvc
  @ComponentScan(basePackages = {"org.swiftboot.web"})
  public class MyDemoConfig {
  }
  ```

或者
  ```java
  @Configuration
  @EnableWebMvc
  public class MyDemoConfig {
    @Bean
    ExceptionProcessor exceptionProcessor() {
        return new ExceptionProcessor();
    }
  }
  ```

如果需要自定义接口异常的代码和消息，只需要抛出 `ErrMessageException` 异常即可，例如：

  ```java
  throw new ErrMessageException("4001", "error message");
  ```

### 输入参数验证


除了常规的异常处理增强之外，SwiftBoot 还实现了 `ValidationExceptionProcessor` 控制器增强来处理验证异常信息的转换。它会捕获验证框架抛出的异常，并把异常转换为 SwiftBoot 定义的 `JSON` 输出格式。（自动从 Request 对象的 `ApiModelProperty` 注解中获取到参数对应的描述信息）
配置：
  ```java
  @Bean
  ValidationExceptionProcessor validationExceptionProcessor() {
      return new ValidationExceptionProcessor();
  }
  ```

如果接口参数中有 `BindingResult` 这个参数，那么验证异常就不会抛出，此时可以在控制器类上添加注解 `@ConvertValidateResult` 来标识需要拦截并抛出 `ValidationException` 异常。
> 这个注解也可以加在控制器方法上，只有该方法执行的时候才会进行增强处理。

  ```java
  @Controller
  @RequestMapping("/order")
  @ConvertValidateResult
  public class OrderController {
  }
  ```

### HTTP 头处理

只要你的接口参数对象继承了 `HttpRequest` 类或者其子类，SwiftBoot 就会自动把 `HttpServletRequest` 中的 Header 添加到对象中，通过调用 `getHeader()` 方法就可以得到 Header 值：

```java
public class MyRequest extends HttpRequest {
}

String myHeader = myRequest.getHeader("my_header");
```


### 其他
* 开启 CORS 跨域
```yaml
swiftboot:
  web:
    filter:
      cors: true
```

> 开启后跨域访问不受限制，仅用于开发调试，生产环境如果需要跨域访问则必须自行配置 Spring 的跨域过滤器 `CorsFilter`
