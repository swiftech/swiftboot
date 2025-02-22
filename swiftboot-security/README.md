# SwiftBoot-Security

快速实现基于 `SpringSecurity` 的用户认证和鉴权

## 认证

SwiftBoot-Security 提供了一个基于 JWT 的过滤器，实现了基本的用户的认证管理

步骤：
* 实现登录接口
  * 实现 `UserDetailsService` 接口，实现 `loadUserByUsername` 方法查询当前登录用户是否合法。
  * 在你的登录 Controller 里实现登录接口，其中调用 `jwtTokenProvider` 生成 Access Token 并返回给客户端。
  ```java
  String accessToken = jwtTokenProvider.generateToken(userName);
  ```

* 配置过滤器
  在配置 `SecurityFilterChain` 中添加过滤器使其生效：
```java
httpSecurity.addFilterAfter(jwtAuthenticationFilter, LogoutFilter.class);
```

* API请求
用户登录成功后会得到一个 Access Token，后续请求将这个 Token 放入请求头中

```
Authorization=Bearer <access token>
```

> 参考demo：[swiftboot-demo-security](../swiftboot-demo/swiftboot-demo-security)

## 鉴权

