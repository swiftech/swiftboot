# SwiftBoot-Auth V3 高级

### Session 模式：

一旦启用了 Session 模式，用户认证之后的用户 Token 会被自动写入到 Cookie 或者 Header 中返回给客户端，
如果写入的是 Header，那么客户端从接口返回的 Header 中取得 Token 后需要放入后续请求的 Header 中，名称和配置的 `swiftboot.auth.tokenKey` 一样，例如 `user_token`。
其内建的过滤器就会检查请求 `Header` 或 `Cookie` 中是否存在令牌（Token）以及令牌对应的会话（session）是否有效，通过则允许访问，
令牌的名称通过 `swiftboot.auth.tokenKey` 来设定，过滤器需要过滤哪些 API 通过在配置 `FilterRegistrationBean` 中注册相应的 URI 来指定（例子请看后面的章节）


```java
@Resource
private UserAuthService userAuthService;

@PostMapping(value = "signin")
public AuthenticatedResponse<AppUserSignInDto, Session> userSignIn(@RequestBody UserSignInRequest cmd) {
    Authenticated authenticated = userAuthService.userSignIn(request.getLoginName(), request.getLoginPwd());
    if (authenticated instanceof Session session) {
        String userId = ...
        String username = ...

        AppUserSignInDto signInDto = new AppUserSignInDto();
        signInDto.setId(userId);
        signInDto.setUserName(userName);
        signInDto.setAccessToken(session.getUserToken());
        signInDto.setExpiresAt(session.getExpireTime());
        return response;
    }
}
```


#### 用户认证后的处理

* 配置过滤器

  对于需要登录才能访问的资源路径，按照 SpringBoot 提供的方式配置过滤器 `org.swiftboot.auth.filter.SessionAuthFilter`

```java
@Resource
private org.swiftboot.auth.filter.SessionAuthFilter authFilter;

@Bean
public FilterRegistrationBean<AuthFilter> regAuthFilter() {
    FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(authFilter);
    registrationBean.addUrlPatterns("/secure/api/*");
    registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
    return registrationBean;
}
```



#### 配置 application.yaml 例子

```yaml
swiftboot:
  auth:
    authType: session
    tokenKey: user_token
    session:
      type: redis
      group: user_session
      expiresIn: 1800
      updateExpireTime: true
      useCookie: true
      cookiePath: /
```

参数解释：

* authType
  认证模式，可选 `jwt` 和 `session`，不配置的话不启用

* type
  配置为 `redis`, 使用 Redis 存储会话，需要配置 `RedisService`，参考：[swiftboot-service](../../swiftboot-service/docs/v3.md)。
  配置为 `mock`，使用内存存储会话，仅用于调试。

* group
  默认的会话分组名称，用这个值来创建会话存储（Redis）中的集合。

* tokenKey
  传递用户认证后得到的 Token 的 Key 的名称，无论是在 Cookie 还是在 Header 中都是用这个名字进行存储，默认为 `access_token`。

* expiresIn
  会话超时时间长度，单位秒，默认 `1800` 秒（即30分钟）

* updateExpireTime
  当用户访问的时候是否更新会话的超时时间（只在 `expiresIn` > 0 的时候有效），默认为 `false`。

* useCookie
  是否将用户 Token 写入 Cookie，默认为 `false` 为写入 Header

* cookiePath
  Cookie 中用户 Token 的有效路径，默认为 `/`，只有在 `useCookie` 为 `true` 时有效。


### 自动获取用户 ID 和名称

对于已登录的用户的请求，用户 ID 会被自动的注入，无需写代码从 Header 或者 Cookie 获取用户 Token，再从会话中获取用户 ID 了。

* 对于 `POST` 请求，只要是继承自 `BaseAuthenticatedRequest` 或者其子类的接口参数对象，都会被自动的注入用户ID和用户名称，在 Controller 中只要通过 `request.getUerId()` 和 `request.getUserName()` 就可以获得。例如：

```java
public class OrderCreateRequest extends BaseAuthenticatedRequest<OrderEntity> {

}
```

```java
@RequestMapping(value = "order/create", method = RequestMethod.POST)
public HttpResponse<OrderCreateResult> orderCreate(@RequestBody OrderCreateRequest request) {
    log.info(request.getUserId());
    log.info(request.getUserName());
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

