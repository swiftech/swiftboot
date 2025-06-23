# SwiftBoot-Web 高级


### 请求对象参数自动填充

* 输入参数对象继承 `BasePopulateRequest`，并指定范型类型为对应的实体类的类型，例如 `BasePopulateRequest<OrderEntity>`。
* 对于新增数据的操作，调用 `createEntity()` 方法即可实例化相对应的实体类，并把输入参数对象中所有名称对应的值填充到实体类中。对于用 `@OneToOne` 和 `@OneToMany` 注解标注的子类或者子集合也会被自动填充。
* 对于修改数据的操作，查询出需要修改的实体类之后，调用 `populateEntity()` 方法将输入参数对象中所有名称对应的值填充到实体类中。 对于用 `@OneToOne` 和 `@OneToMany` 注解标注的子类或者子集合也会被自动填充。


#### 子集合的参数自动填充

对于编辑一个对象时的子集合的增删改查，无需对子对象分别进行各自的处理，只需要查询父实体并对其子实体集合执行 `clear()` 操作， 然后再调用 `populateEntity()` 将参数中的子集合填充至父实体中，SwiftBoot 会自动判断实体是否存在，如果已经存在则进行更新，如果不存在则新增，其余不在子集合中的实体都会被删除。
例如：


父参数对象定义，包含子对象的集合：
  ```java
  public class OrderRequest extends BasePopulateRequest<OrderEntity> {
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
  public class OrderEntity extends BaseIdEntity {
      @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order", orphanRemoval = true)
      private List<OrderDetailEntity> details;
  }
  ```

实现业务逻辑：
  ```java
  public void editOrder(OrderRequest request) {
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


### DTO 参数的自动填充
* 返回值对象 DTO 通常要继承 `BasePopulateDto` 类就可以利用自动填充功能。如果需要避免继承关系获得更多的灵活性，DTO 类可以不继承 `BasePopulateDto` 但是必须实现 `PopulatableDto` 接口。
* 对于继承 `BasePopulateDto` 的方式，在代码中直接实例化 DTO 实例，然后调用它的 `populateByEntity()` 方法进行填充。
* 对于实现 `PopulatableDto` 方式，在需要创建 DTO 实例的地方调用 `PopulatableDto` 的静态方法 `createDto()` 即可实例化 DTO，并把将查询到的实体类中所有对应名称的值填充到输出对象中。
* 如果实体类定义了一对一、一对多关联，那么 DTO 类也可以定义相应的关联关系（变量名相同），这样的关联对象也会被自动填充
* 有时候虽然 DTO 对象之间定义了关联关系，但是你可能不希望在所有的场景下都自动的填充关联的对象，那么 `populateByEntity()` 方法提供了一个 `includeRelation` 参数可以覆盖默认的填充行为，只对当前 DTO 类的属性进行填充，而不会处理关联对象（包括一对一，多对一或者一对多关系）。


### Java8 时间类型的处理

SwiftBoot-Web 提供了对 Java8 时间类型的支持，包括 `LocalDateTime`，`LocalDate`，`LocalTime`，`YearMonth`，`MonthDay`。
例如对于 `LocalDateTime` 类型的时间，输入参数只需要提供格式为 `2025-05-01 11:11:11` 这样的字符串，就会自动转换为 `LocalDateTime` 类型，对于 DTO 来说也是类似。
如果需要使用其他时间格式，只需要在配置文件中定义即可：
```
swiftboot:
  web:
    formatPatternLocalDateTime: yyyy-MM-dd HH:mm:ss
```
> 其他的时间类型格式参考 [application-example.yaml](../src/main/resources/application-example.yaml)


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

> 如果需要自定义错误信息，那么可以传入第二个参数： `new Response(ResponseCode.CODE_PARAMS_ERROR, "我的错误信息");`，
> 如果资源存在参数化：`new ResponseBuilder<Void>().code(ResponseCode.CODE_PARAMS_ERROR).message("我的错误信息: {0} {1}").messageArgs("参数0", "参数1"")";`
> `ResponseBuilder` 也可以创建非参数化的 `Response`

如果需要自定义错误代码，那么实现一个 Bean，将错误代码定义以静态字符串变量放入这个 Bean 中，变量名需要以 `CODE_` 开头，并在启动时进行加载，例如：

```java
@Component
public class ErrorCode {

    public static final String CODE_MY_ERR_CODE1 = "4000";
    public static final String CODE_MY_ERR_CODE2 = "4001";

    @Resource
    private ResponseCode responseCode;

    @PostConstruct
    public void init() {
        try {
            responseCode.loadFromClass(this.getClass());
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
