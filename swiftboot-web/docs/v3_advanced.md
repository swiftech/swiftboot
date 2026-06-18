# SwiftBoot-Web 高级


### 请求对象参数自动填充

* 输入参数对象继承 `BasePopulateRequest`，并指定范型类型为对应的实体类的类型，例如 `BasePopulateRequest<OrderEntity>`。
* 对于新增数据的操作，调用 `createEntity()` 方法即可实例化相对应的实体类，并把输入参数对象中所有名称对应的值填充到实体类中。对于用 `@OneToOne` 和 `@OneToMany` 注解标注的子类或者子集合也会被自动填充。
* 对于修改数据的操作，查询出需要修改的实体类之后，调用 `populateEntity()` 方法将输入参数对象中所有名称对应的值填充到实体类中。 对于用 `@OneToOne` 和 `@OneToMany` 注解标注的子类或者子集合也会被自动填充。


#### 子集合的参数自动填充

对于编辑一个对象时的子集合的增删改查，无需对子对象分别进行处理，只需要查询父实体并对其子实体集合执行 `clear()` 操作， 然后再调用 `populateEntity()` 将参数中的子集合填充至父实体中，SwiftBoot 会自动判断实体是否存在，如果已经存在则进行更新，如果不存在则新增，其余不在子集合中的实体都会被删除。
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

如果有哪个属性不需要（或者无法）自动填充的，在属性上使用 `@PopulateIgnore` 注解来忽略自动填充。
例如：
```java
@PopulateIgnore
private boolean password;
```

### 返回值
接口的返回值可以通过构造 `org.swiftboot.web.response.Response` 对象包含DTO对象来实现，例如
> 这不是必须的，你也可以用 Spring Web 原生的 ResponseEntity 来返回值，只是你不再能利用 SwiftBoot 提供的相关功能了。

* 直接构造：
```java
OrderDto dto = new OrderDto();
return new Response(dto);
```

* 通过构造器构造
```java
OrderDto dto = new OrderDto();
return Response.builder(OrderDto.class).data(dto).build();
```

### DTO 参数的自动填充
* 返回值对象 DTO 通常要继承 `BasePopulateDto` 类就可以利用自动填充功能。如果需要避免继承关系获得更多的灵活性，DTO 类可以不继承 `BasePopulateDto` 但是必须实现 `PopulatableDto` 接口。
* 对于继承 `BasePopulateDto` 的方式，在代码中直接实例化 DTO 实例，然后调用它的 `populateByEntity()` 方法进行填充。
* 对于实现 `PopulatableDto` 方式，在需要创建 DTO 实例的地方调用 `PopulatableDto` 的静态方法 `createDto()` 即可实例化 DTO，并把将查询到的实体类中所有对应名称的值填充到输出对象中。
* 如果实体类定义了一对一、一对多关联，那么 DTO 类也可以定义相应的关联关系（变量名相同），这样的关联对象也会被自动填充
* 有时候虽然 DTO 对象之间定义了关联关系，但是你可能不希望在所有的场景下都自动的填充关联的对象，那么 `populateByEntity()` 方法提供了一个 `includeRelation` 参数可以覆盖默认的填充行为，只对当前 DTO 类的属性进行填充，而不会处理关联对象（包括一对一，多对一或者一对多关系）。


### 错误处理

构造 `org.swiftboot.web.response.Response` 对象可以将错误代码传入返回给前端，`org.swiftboot.web.response.ResponseCode` 这个类提供了一些常见的错误代码（从 3000 开始）。例如：

```java
return new Response(ResponseCode.CODE_PARAMS_ERROR); // 返回 '输入参数错误' 给客户端
```

以上代码会返回如下数据给前端（假设客户端语言为简体中文）：

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


### Java8 时间类型的处理

SwiftBoot-Web 提供了对 Java 8 的时间类型的支持，包括 `LocalDateTime`，`LocalDate`，`LocalTime`，`YearMonth`，`MonthDay`。
例如对于接口要求的 `LocalDateTime` 类型的时间参数，客户端只需要提供类似 `2025-05-01 11:11:11` （默认为 `yyyy-MM-dd HH:mm:ss`格式）这样的字符串，就会自动转换为 `LocalDateTime` 类型，对于 DTO 的参数来说也是类似。
不管是 `GET` 接口直接传参时间变量，或者是接口定义请求参数对象中的时间属性，都支持自动转换。
如果需要使用其他时间格式，只需要在配置文件中定义即可：
```yaml
swiftboot:
  web:
    formatPatternLocalDateTime: yyyy/MM/dd HH:mm:ss
```
其他的时间类型配置分别如下：
```yaml
swiftboot:
  web:
    formatPatternLocalDate: yyyy-MM-dd
    formatPatternLocalTime: HH:mm:ss
    formatPatternYearMonth: yyyy-MM
    formatPatternMonthDay: MM-dd
```
> 参考 [application-example.yaml](../src/main/resources/application-example.yaml)


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
> 如果第二个参数不传，则会尝试从资源文件中找到对应的字符串资源。


### 多语言

* 定义 `MessageSource`，加入自定义资源文件的名称。
> 从3.1.1开始，不需要再把 SwiftBoot 内部定义的资源文件加进去了，只需要配置自定义的文件即可。

```java
@Bean
public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasenames("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setFallbackToSystemLocale(false); // not using system default locale.
    return messageSource;
}
```

* 默认情况下 SpringBoot 会按照系统的语言设定去读取相应语言的资源文件，如果需要强制按照某种语言读取，可以在应用程序启动的地方设定系统默认语言，例如：

```java
Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
```

* SpringBoot 会按照客户端给出的语言参数 `Accept-Language` 去读取相应语言的资源，如果客户端给出了语言参数，它的优先级高于前面设置的默认语言。
> 通过 `LocaleContextHolder.getLocale()` 可以得到当前请求的语言。


### 输入参数验证


除了常规的异常处理增强之外，SwiftBoot 还实现了 `ValidationExceptionProcessor` 控制器增强来处理验证异常信息的转换。它会捕获验证框架抛出的异常，并把异常转换为 SwiftBoot 定义的 `JSON` 输出格式。（自动从 Request 对象的属性定义上的 `@Schema` 注解中获取到参数对应的描述信息）
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

如果需要让属性名称支持多语言，只要配置
```yaml
swiftboot:
  web:
    validation:
      i18n: true
```
然后用 `@Schema` 注解属性的时候，用资源代码替代直接的文字描述，例如：
```java
@Schema(description = "i8n.validation.login.username")
```
> 最后别忘了在资源文件中添加对应的多语言文字描述

### HTTP 头处理

只要你的接口参数对象继承了 `HttpRequest` 类或者其子类，SwiftBoot 就会自动把 `HttpServletRequest` 中的 Header 添加到对象中，通过调用 `getHeader()` 方法就可以得到 Header 值：

```java
public class MyRequest extends HttpRequest {
}

String myHeader = myRequest.getHeader("my_header");
```

### 接口限流

从v3.1.1开始，新增的注解 `@RateLimit` 可用于给特定接口限流，或者给特定接口针对用户进行限流。

例如，限制一个接口1秒钟之内只能被请求10次：
```java
@RateLimit(time = 1000, count = 10)
public Response<String> limitedEndpoint() {
}
```

例如，限制一个用户在5秒钟之内请求此接口1次：
```java
@RateLimit(time = 5000, count = 1, limitType = LimitType.USER)
public Response<String> userLimitedEndpoint() {
}
```

> 注意：通过反向代理服务器（如nginx）提供API的情况下，有可能会拿到本机的IP地址，所以如果要针对个人进行流量控制的话，必须保证反向代理服务器可以给出正确的IP地址，否则可能会导致所有未登录的用户被限流。

从v3.1.4开始，除了注解方式外，还可以通过配置文件定义限流规则，按 URI 路径匹配进行限流，无需修改代码：

```yaml
swiftboot:
  web:
    rate-limit:
      rules:
        - uri: /health/**
          time: 1000
          count: 10
          limit-type: DEFAULT
        - uri: /api/user/**
          time: 5000
          count: 5
          limit-type: USER
```

配置说明：
* `uri`：Ant 风格的路径匹配模式。
* `time`：时间窗口（毫秒），默认 1000。
* `count`：时间窗口内允许的最大请求次数，默认 10。
* `limit-type`：限流类型，`DEFAULT` 为全局限流，`USER` 为针对用户限流。

> 注解和配置文件两种方式可以同时使用，互不冲突。

### 模拟 API 超时

从v3.1.3开始，新增全局 API 延迟功能，用于测试前端对接口超时的处理。开启后所有 API 请求都会被强制延迟指定的时间。

配置方式：

```yaml
swiftboot:
  web:
    mock:
      mockTimeout: true        # 开启后所有接口都会延迟响应
      timeout: 10000           # 延迟时间（毫秒），默认 10000（10秒）
```

> 此功能仅用于开发和测试环境，切勿在生产环境开启。

### 其他
* 开启 CORS 跨域
```yaml
swiftboot:
  web:
    filter:
      cors: true
```

> 💡 注意：开启后跨域访问不受限制，仅用于开发调试。生产环境不建议开启，设置为 `cors: false`，如果要跨域访问则必须自行配置 Spring 的跨域过滤器 `CorsFilter` 来实现多个域名的跨域请求。
