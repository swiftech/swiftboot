# SwiftBoot-Auth

提供 Web 应用开发中的用户认证功能

### 特性
* 非常少的代码即可实现完整的用户认证，只要实现校验用户凭证的代码逻辑和配置认证过滤有效的 URL 即可。

* 一旦引用了 SwiftBoot-Auth，其内建的过滤器就会检查请求 Header 或 Coolie 中是否存在令牌（Token）以及令牌对应的会话（session）是否有效，
令牌的名称通过 `swiftboot.auth.session.tokenKey` 来设定，过滤器需要过滤哪些 API 通过在配置 `FilterRegistrationBean` 中注册相应的 URI 来指定（例子请看后面的章节）

* 在实现了用户身份校验之后，生成一个全局唯一的 token（建议用 UUID），调用 `SessionService.addSession(String token, Session session)` 来缓存用户的会话。
成功之后将 token 值写入到 cookie 中或者返回给客户端，由客户端保存并加入到请求的 header 中。
 
### Maven

```xml
<dependency>
    <groupId>com.github.swiftech</groupId>
    <artifactId>swiftboot-auth</artifactId>
    <version>2.1</version>
</dependency>

```
	
### 使用方法

* 处理用户登录

```java
@Service
public class MySigninServiceImpl {
    
    @Resource
    private SessionService sessionService;

    public String doSignin(){
        // 用户登录认证逻辑
        ...... 
        // 用户认证通过的话保存用户会话
        String userId = "<my user id>";
        String token = IdUtils.makeUUID(); // replace with your own token mechanism
        Session session = new SessionBuilder().userId(userId).userName(nickName).createSession();
        sessionService.addSession(token, session);
        return token;
    }
}
```

* 配置过滤器
    
    对于需要登录才能访问的资源路径，按照 SpringBoot 提供的方式配置过滤器 `org.swiftboot.auth.filter.AuthFilter`

```java
public class MyConfig {

    @Resource
    org.swiftboot.auth.filter.AuthFilter authFilter;
    
    @Bean
    public FilterRegistrationBean<AuthFilter> regAuthFilter() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(authFilter);
        registrationBean.addUrlPatterns("/secure/api/*");
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registrationBean;
    }
}
```

* 在 Controller 中将分配给用户的 Token 写入 Cookie 或者直接返回给客户端（客户端放入后续请求的 Header 中，名称和配置的 `swiftboot.auth.session.tokenKey` 一样，例如 `my_token` ）

```java
Cookie cookie  = new Cookie(config.getSession().getTokenKey(), adminUserResult.getToken());
httpServletResponse.addCookie(cookie);
```

* 自动获取用户 ID

对于已登录的用户的请求，用户 ID 会被自动的注入，无需写代码从 Cookie 或者 Header 获取用户 Token，再从会话中获取用户 ID 了。

  * 对于 `POST` 请求，只要是继承自 `BaseAuthenticatedCommand` 或者其子类的接口参数对象，都会被自动的注入用户ID和用户名称，在 Controller 中只要通过 `command.getUerId()` 和 `command.getUserName()` 就可以获得。例如：

```java
public class OrderCreateCommand extends BaseAuthenticatedCommand<OrderEntity> {

}
```

```java
@RequestMapping(value = "order/create", method = RequestMethod.POST)
public HttpResponse<OrderCreateResult> orderCreate(@RequestBody OrderCreateCommand command) {
    log.info(command.getUserId());
    log.info(command.getUserName());
}
```
  * 对于 `GET` 请求，只需要在 Controller 方法上加一个 `String` 类型的参数，并且加上 `@UserId` 注解即可得到用户ID。例如：

```java
public HttpResponse<?> getOrderList(@UserId String userId) {
    log.info(userId);
}
```

> 除了 `@UserId`，还有 `@UserName` 可以直接获得登录的用户名，`@ExpireTime`可以获得会话超时的时间点(类型为long)，或者可以通过 `@UserSession` 直接获得 session 对象

> 如果不想用以上方式获得会话中的信息，也可以通过直接继承 `BaseAuthController`，调用 `fetchUserIdFromSession` 方法来拿到用户的ID。


* 配置 application.yaml 例子

```yaml
swiftboot:
  auth:
    enabled: true
    session:
      type: redis
      group: my_session
      tokenKey: my_token
      expiresIn: 1800
      updateExpireTime: true
```

  参数解释：

  * enabled
      是否启用，如果为 `false`，则 swiftboot-auth 定义的 Bean 都不会加载，功能也都无效。

  * type
      配置为 `redis`, 使用 Redis 存储会话，需要配置 `RedisService`，参考：[swiftboot-service](../swiftboot-service/README.md)。
      配置为 `mock`，使用内存存储会话，仅用于调试。

  * group
      默认的会话分组名称，用这个值来创建会话存储（Redis）中的集合，默认为 `swiftboot_token`。

  * tokenKey
      传递用户认证后得到的 Token 的 Key 的名称，无论是在 Cookie 还是在 Header 中都是用这个名字进行存储。 

  * expiresIn
      会话超时时间长度，单位秒，默认 `1800` 秒（即30分钟）

  * updateExpireTime
    当用户访问的时候是否更新会话的超时时间（只在 `expiresIn` > 0 的时候有效），默认为 `false`。