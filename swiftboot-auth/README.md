# SwiftBoot-Auth
提供企业开发中的基本的用户认证功能）

### 特性
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

* application.yaml

```yaml
swiftboot:
  auth:
    session:
      type: redis
      group: my_session
      tokenKey: my_token
      expireTime: 1800
```

    参数解释：

    * type
        配置为 `redis`, 使用 Redis 存储会话，需要配置 `RedisService`，参考：[swiftboot-service](../swiftboot-service/README.md)
        配置为 `mock`，使用内存存储会话，仅用于调试.

    * group

    * tokenKey

    * expireTime

* 配置过滤器

```java
public class MyConfig {
    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }
    @Bean
    public FilterRegistrationBean<AuthFilter> regAuthFilter() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(authFilter());
        registrationBean.addUrlPatterns("/secure/api/*");
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registrationBean;
    }
}
```

* 处理用户登录

```java
@Service
public class MySigninServiceImpl {
    
    @Resource
    private SessionService sessionService;

    public String doSignin(){
        // 用户登录验证逻辑
        ...... 
        // 保存用户会话
        String userId = "<my user id>";
        String token = IdUtils.makeUUID(); // replace with your own token mechanism
        Session session = new Session();
        session.setUserId(userId);
        sessionService.addSession(token, session);
        return token;
    }
}
```

* 在 Controller 中写入 Cookie 或者返回给客户端（客户端放入后续请求的 Header 中，名称和配置的 `swiftboot.auth.session.tokenKey` 一样，例如 `my_token` ）

```java
Cookie cookie  = new Cookie(config.getSession().getTokenKey(), adminUserResult.getToken());
httpServletResponse.addCookie(cookie);
```

* 自动获取用户 ID

对于已登录的用户的请求，用户 ID 会被自动的注入，无需写代码从 Cookie 或者 Header 获取用户 Token，再从会话中获取用户 ID 了。

  * 对于 `POST` 请求，只要是继承自 `BaseSessionCommand` 的接口参数对象，都会被自动的注入用户ID，在 Controller 中只要通过 `command.getUerId()` 就可以获得 ID。例如：

```java
public class OrderCreateCommand extends BaseSessionCommand<OrderEntity> {

}
```

```java
@RequestMapping(value = "order/create", method = RequestMethod.POST)
public HttpResponse<OrderCreateResult> orderCreate(@RequestBody OrderCreateCommand command) {
    log.info(command.getUserId());
}
```
  * 对于 `GET` 请求，只需要在 Controller 方法上加一个 `String` 类型的参数，并且加上 `@UserId` 注解即可得到用户ID。例如：

```java
public HttpResponse<?> getOrderList(@UserId String userId) {

}
```

> 除了 `@UserId`，还有 `@UserName` 可以直接获得登录的用户名，甚至可以通过 `@UserSession` 直接获得 session 对象