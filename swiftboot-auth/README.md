# swiftboot-auth
提供企业开发中的基本的用户认证功能（1.1版本加入）

### 特性
* 一旦引用了 swiftboot-auth，其内建的过滤器就会检查请求 header 或 cookie 中是否存在令牌（token）以及令牌对应的会话（session）是否有效，
令牌的名称通过 `swiftboot.auth.session.tokenKey` 来设定，过滤器需要过滤哪些 API 通过在配置 `FilterRegistrationBean` 中注册相应的 URI 来指定（例子请看后面的章节）

* 在实现了用户身份校验之后，生成一个全局唯一的 token（建议用 UUID），调用 `SessionService.addSession(String token, Session session)` 来缓存用户的会话。
成功之后将 token 值写入到 cookie 中或者返回给客户端，由客户端保存并加入到请求的 header 中。
 
### Maven

```xml
<dependency>
    <groupId>com.github.swiftech</groupId>
    <artifactId>swiftboot-auth</artifactId>
    <version>1.2</version>
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

* 配置过滤器

```java
public class MyConfig {
    @Bean
    public AuthFilter securityFilter() {
        return new AuthFilter();
    }
    @Bean
    public FilterRegistrationBean<AuthFilter> regAuthFilter() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(AuthFilter());
        registrationBean.addUrlPatterns("/secure/api/*");
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registrationBean;
    }
}
```

* 处理用户登录

```java
public class MySigninServiceImpl {
    
    @Resource
    private SessionService sessionService;

    public String doSignin(){
        // 用户登录验证逻辑
        ...... 
        // 保存用户会话
        String userId = "<my user id>";
        String token = "<my token>";
        Session session = new Session(userId);
        sessionService.addSession(token, session);
        return token;
    }
}
```

* 写入 Cookie 或者返回给客户端（客户端放入后续请求的 Header 中，名称和配置的 `swiftboot.auth.session.tokenKey` 一样 ）

```java
    Cookie cookie  = new Cookie(config.getSession().getTokenKey(), adminUserResult.getToken());
    httpServletResponse.addCookie(cookie);
```