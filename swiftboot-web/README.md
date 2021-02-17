# SwiftBoot-Web
SwiftBoot 的主模块，开发 Web 应用需要引用。

## 依赖
* JDK >= 1.8
* Servlet >= 3.1
* Spring Framework >= 5.2.x.RELEASE
* Spring MVC >= 5.2.x.RELEASE
* Spring Boot >= 2.2.x.RELEASE
* Spring Data JPA >= 2.2.x.RELEASE


## 特性
* 开箱即用，用更少的代码完成更多的工作。
* 是无侵入性的，你可以只使用其中的一部分功能，也可以随时去掉它或者切换为别的框架。
* 统一固定的返回值格式:
  ```json
  {
    "code": "<4位的错误代码>",
    "msg" : "<错误消息>",
    "content": {
      "自定义的JSON格式的返回内容"
    }
  }
  ```
* 可实现将接口参数自动填充到实体类中。
* 可实现自动将实体类的属性值填充到返回值中。
* 提供了统一的控制器（Controller）异常处理，自动将未处理的异常转换成 `JSON` 格式的接口响应对象返回给客户端。
* 自动处理接口参数验证结果，转换为 `JSON` 格式的统一格式；扩展的表单验证器，可验证：手机号，包含大写数字，包含数字，包含特殊符号。

## 引用 jar 包：

  Maven:

  ```xml
  <dependency>
    <groupId>com.github.swiftech</groupId>
    <artifactId>swiftboot-web</artifactId>
    <version>2.0.0</version>
  </dependency>
  ```

## 如何使用

### 配置

* 配置


  ```java
  @Configuration
  @ComponentScan(basePackages = {"org.swiftboot.web", "org.swiftboot.data"})
  @EntityScan(basePackages = {"x.x.x"})
  @EnableJpaRepositories(basePackages = {"x.x.x"})
  public class MyConfig {
  }
  ```
 
### Model层
* DAO 接口
  

  ```java
  public interface OrderDao extends CrudRepository<OrderEntity, String> {
  }
  ```

* 实体类定义


  ```java
  @Entity
  @Table(name = "DEMO_ORDER")
  public class OrderEntity extends BaseEntity {
      @Column(name = "ORDER_CODE", length = 16, columnDefinition = "COMMENT '订单编号'")
      private String orderCode;

      @Column(name = "DESCRIPTION", length = 64, columnDefinition = "COMMENT '订单描述'")
      private String description;
  }
  ```

[详细参考](../swiftboot-data/README.md)

### 控制器 Controller

* 所有的控制器接口返回统一定义的响应对象 `HttpResponse`，包含错误代码、错误消息以及泛型表达的接口返回值。`POST`, `PUT` 和 `PATCH` 接口的所有输入参数对象继承 `HttpCommand` 或者它的子类。


  例如一个创建订单的接口如下：
  ```java
  @RequestMapping(value = "order/create", method = RequestMethod.POST)
  public
  @ResponseBody HttpResponse<OrderCreateResult> orderCreate(
          @RequestBody @Validated @ApiParam("创建订单参数") OrderCreateCommand command) {
      OrderCreateResult ret = orderService.createOrder(command);
      return new HttpResponse<>(ret);
  }
  ```

  SpringMVC 的 `@ResponseBody` 注解会把方法返回的 `HttpResponse` 对象及其内嵌的对象一起转换成 JSON 格式返回给访问接口的客户端。

  ```java
  @ApiModel
  public class OrderCreateCommand extends BasePopulateCommand<OrderEntity> {
    @ApiModelProperty(value = "订单编号", example = "2019032411081201")
    @JsonProperty("order_code")
    @Length(max = 16)
    private String orderCode;

    @ApiModelProperty(value = "订单描述", example = "越快越好")
    @JsonProperty("description")
    @Length(max = 64)
    private String description;
  }
  ```

* 控制器中抛出的异常直接抛出会使得客户端的错误展示非常不友好，而通过代码去捕获即繁琐又容易遗留，SwiftBoot 实现了控制器增强 `ExceptionProcessor`，他将异常信息以统一的 `JSON` 格式输出给客户端，配置方法如下：
  

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

* 输入参数验证


  除了常规的异常处理增强之外，SwiftBoot 还实现了 `ValidationExceptionProcessor` 控制器增强来处理验证异常信息的转换。它会捕获验证框架抛出的异常，并把异常转换为 SwiftBoot 定义的 `JSON` 输出格式。（自动从 Command 对象的注解中获取到参数对应的描述信息）
  配置：
  ```java
  @Bean
  ValidationExceptionProcessor validationExceptionProcessor() {
      return new ValidationExceptionProcessor();
  }
  ```

  如果接口参数中有 `BindingResult` 这个参数，那么验证异常就不会抛出，此时可以在控制器类上添加注解 `@ConvertValidateResult` 来标识需要拦截并抛出 `ValidationException` 异常。这个注解也可以加在控制器方法上，只有该方法执行的时候才会进行增强处理。

  ```java
  @Controller
  @RequestMapping("/order")
  @ConvertValidateResult
  public class OrderController {
  }
  ```


* HTTP 头处理

SwiftBoot 可以帮助你把 HttpServletRequest 中的 Header 自动添加到 Command 对象中, 便于取用


  ```java
  @EnableWebMvc
  public class MyDemoConfig implements WebMvcConfigurer{
    
      public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
          Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
          converters.add(new MessageConverter(builder.build));  
      }
  }
  ```

### Service 层

#### 参数对象自动填充

Web 开发中最无趣的工作之一就是从接口参数对象中复制每个参数值到 Model 层的实体类中进行保存，反之亦然。 SwiftBoot 实现了自动化的参数填充，它能够有选择性的将参数值填充到对应的实体类中，也能将实体类中的值填充到返回值对象中（如果实体类关联了其他实体类对象，它也会对应的填充到返回值对象的内嵌对象中去）。
* 输入参数自动填充实现方法：
  * 输入参数对象继承 `BasePopulateCommand`，并指定范型类型为对应的实体类的类型。
  * 对于新增数据的操作，调用 `createEntity()` 方法即可实例化相对应的实体类，并把输入参数对象中所有名称对应的值填充到实体类中。对于用 `@OneToOne` 和 `@OneToMany` 注解标注的子类或者子集合也会被自动填充。
  * 对于修改数据的操作，查询出需要修改的实体类之后，调用 `populateEntity()` 方法将输入参数对象中所有名称对应的值填充到实体类中。 对于用 `@OneToOne` 和 `@OneToMany` 注解标注的子类或者子集合也会被自动填充。
* 输出参数自动填充实现方法：
  * 返回值对象继承 `BasePopulateResult`
  * 在需要的地方调用 `BasePopulateResult` 的静态方法 `createResult()` 即可实例化返回值对象，并把将查询到的实体类中所有对应名称的值（包括一对一、一对多关联的实体类）填充到输出对象中。或者在代码中直接实例化返回对象实例，然后调用它的 `populateByEntity()` 方法进行填充。

* 创建和查询代码示例：


  ```java
  @Service
  public class OrderServiceImpl implements OrderService {
    @Override
    public OrderCreateResult createOrder(OrderCreateCommand cmd) {
        OrderEntity p = cmd.createEntity();
        p.setId(IdUtils.makeUUID()); // 如果设置了自动生成 ID 就不需要这一行代码
        OrderEntity saved = orderDao.save(p);
        return new OrderCreateResult(saved.getId());
    }

    @Override
    public OrderResult queryOrder(String orderId) {
        Optional<OrderEntity> optEntity = orderDao.findById(orderId);
        if (optEntity.isPresent()) {
            return OrderResult.createResult(OrderResult.class, optEntity.get());
        }
        return null;
    }
  }
  ```

  输入参数对象类定义：
  ```java
  @ApiModel
  public class OrderCreateCommand extends BasePopulateCommand<OrderEntity> {

    @ApiModelProperty(value = "订单编号", example = "2019032411081201")
    @JsonProperty("order_code")
    @Length(max = 16)
    private String orderCode;

    @ApiModelProperty(value = "订单描述", example = "越快越好")
    @JsonProperty("description")
    @Length(max = 64)
    private String description;
  }
  ```

  返回对象类定义：
  ```java
  public class OrderResult extends BasePopulateResult {
    @ApiModelProperty(value = "订单编号", example = "2019032411081201")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "订单描述", example = "越快越好")
    @JsonProperty("description")
    private String description;
  }
  ```

* 子集合的参数填充代码示例：

  对于编辑一个对象时的子集合的增删改查，无需对子对象分别进行各自的处理，只需要查询父实体并对其子实体集合执行 `clear()` 操作， 然后再调用 `populateEntity()` 将参数中的子集合填充至父实体中，SwiftBoot 会自动判断实体是否存在，如果已经存在则进行更新，如果不存在则新增，其余不在子集合中的实体都会被删除。
  例如：
  
  
  父参数对象定义，包含子对象的集合：
  ```java
  public class OrderSaveCommand extends BasePopulateCommand<OrderEntity> {
    @ApiModelProperty(value = "订单详情")
    private List<OrderDetail> details;
  }
  ```
  
  子参数对象定义：
  ```java
  public class OrderDetail extends BasePopulateCommand<OrderDetailEntity> {
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
  public void editOrder(OrderSaveCommand cmd) {
    Optional<OrderEntity> optEntity = orderDao.findById(cmd.getId());
    if (optEntity.isPresent()) {
        OrderEntity orderEntity = optEntity.get();
        orderEntity.getDetails.clear(); // 清除原有子实体集合
        cmd.populateEntity(orderEntity); // 填充实体，包括子集合中的编辑、新增的对象
        orderDao.save(orderEntity);
    }
  }
  ```

  以上代码实现简化了子集合的操作： 
  * 无需直接操作子集合中的对象就能自动填充子实体。
  * 无需区别处理集合中删除、编辑和新增的子对象。


### 其他
* 开启 CORS 跨域
```yaml
swiftboot:
  web:
    filter:
      cors: true
```
> 开启后跨域访问不受限制，仅用于开发调试，生产环境如果需要跨域访问则必须自行配置 Spring 的跨域过滤器 `CorsFilter`