# SwiftBoot-Auth

提供 Web 应用开发中的用户认证功能

### 特性
* 非常少的代码即可实现完整的用户认证，只要实现校验用户凭证的代码逻辑和配置认证过滤有效的 URL 即可。

* 你只需要实现 UserAuthService 接口实现用户身份校验，并在你的 Controller 方法中嗲用 UserAuthService.userSignIn() 方法，用户 Token 会被自动写入到 Cookie 
或者 Header 中返回给客户端，由客户端保存并加入到请求的 Header 中。

* 一旦引用了 SwiftBoot-Auth，其内建的过滤器就会检查请求 Header 或 Cookie 中是否存在令牌（Token）以及令牌对应的会话（session）是否有效，
  令牌的名称通过 `swiftboot.auth.session.tokenKey` 来设定，过滤器需要过滤哪些 API 通过在配置 `FilterRegistrationBean` 中注册相应的 URI 来指定（例子请看后面的章节）


### Maven

```xml
<dependency>
    <groupId>com.github.swiftech</groupId>
    <artifactId>swiftboot-auth</artifactId>
    <version>2.1.1</version>
</dependency>

```
	
### 使用方法

##### 处理用户认证

* Service

```java
import org.swiftboot.auth.controller.AuthenticatedResponse;

@Service
public class MySigninServiceImpl implements UserAuthService {

      public <T> AuthenticatedResponse<T> userSignIn(String loginId, String loginPwd) {
            // 此处实现用户登录认证逻辑，如果失败抛出异常
          
            // 创建会话并返回给调用者
            String userId = "<my user id>";
            String token = IdUtils.makeID("usrtoken"); // 可以替换成你自己的 token 实现，例如 UUID

            Session session = new SessionBuilder()
                    .userToken(token)
                    .userId(userId) // 必须 
                    .userName(userName) // 可选
                    .addition("some_addition", "some addition in session") // 可以存放额外的会话信息
                    .createSession();
            return new AuthenticatedResponse<>(session);
      }
}
```


* Controller 

  将用户认证后得到的 `AuthenticatedResponse` 对象返回给客户端即可，分配给用户的 Token 会被自动写入 Cookie 或者 Header（取决于 useCookie 是否为 true）
  如果写入的是 Header，那么客户端从接口返回的 Header 中取得 Token 后需要放入后续请求的 Header 中，名称和配置的 `swiftboot.auth.session.tokenKey` 一样，例如 `my_token`

```java
@Resource
private UserAuthService userAuthService;

@RequestMapping(value = "signin", method = RequestMethod.POST)
public HttpResponse<AppUserSigninResult> userSignIn(@RequestBody UserSignInCommand cmd){
        AuthenticatedResponse<UserSignInResult> response = userAuthService.userSignIn(cmd.getLoginName(),cmd.getLoginPwd());
        UserSignInResult result = new UserSigninResult(); // 自定义返回对象
        result.setXXX(xxx);
        response.setResult(result);
        return response
}
```

> `UserSignInResult` 是你自定义的返回对象，根据需要添加。


##### 用户认证后的处理

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
      useCookie: true
      cookiePath: /
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

  * useCookie
      是否将用户 Token 写入 Cookie，默认为 `false` 为写入 Header
  
  * cookiePath
      Cookie 中用户 Token 的有效路径，默认为 `/`，只有在 `useCookie` 为 `true` 时有效。