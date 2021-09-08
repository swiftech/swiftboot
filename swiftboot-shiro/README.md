# SwiftBoot-Shiro

SwiftBoot-Shiro 封装了 Apache Shiro 实现了认证（Authentication）和授权（Authorization）
只需要简单的配置和少量的代码即可集成 Shiro 进行用户认证和权限控制。
它实现了一个通用的 `Realm` 类, 可以同时处理不同的帐号体系的认证和授权。

---
## Maven:

  ```xml
  <dependency>
    <groupId>com.github.swiftech</groupId>
    <artifactId>swiftboot-shiro</artifactId>
    <version>2.0.2</version>
  </dependency>
  ```

## 使用

### 依赖
* shiro
* redis


### 常规
* 启用对于 Shiro 的支持

```yaml
swiftboot:
  shiro:
    enabled: true
```

* 设置会话管理

```yaml
swiftboot:
  shiro:
    session:
      storageType: redis
      timeout: 1800
      forceDisableRedirect: false
      redisGroup: shiro-session
    cookie:
      domain: localhost
      path: /
      name: <you cookie name for shiro token>
      maxAge: 1800
```
    
  * storageType: 会话存储方式，可选项为 `memory`（默认） 或者 `redis`，如果需要用 redis 来存储，那么需要配置 redis 服务器
  * timeout: 会话超时时间,单位秒,默认`1800`秒即`30`分钟
  * redisGroup: Redis 分组名称,默认为 `swiftboot-shiro-session`


* 定义用户（认证）实体类，实现 `UserEntityStub` 接口，


### 认证 (Authentication)
* 实现 `UserAuthDaoStub` 接口，这个实现会最终交由处理最终实际的用户数据的查询（因为支持多种认证机制同时工作）


```java
@Component("my-auth-service-name")
public class DemoUserAuthenticator implements UserAuthStub<DemoUserEntity> {

}
```
注意 `@Component` 注解给出的命名会在登录请求发起时用到。

* 声明 Shiro 过滤器


```java
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/order/**", "authc");
        chainDefinition.addPathDefinition("/**", "anon");
        return chainDefinition;
    }
```

* 实现用户登录方法(Service 或者 Controller 中)


```java
Subject currentUser = SecurityUtils.getSubject();
try {
    currentUser.login(new UsernamePasswordToken(command.getLoginName(), command.getLoginPwd(), "my-auth-service-name"));
} catch (AuthenticationException e) {
    e.printStackTrace();
    throw new ErrMessageException(ErrorCodeSupport.CODE_SIGNIN_FAIL, e.getMessage());
}
```
注意: `UserNamePasswordToken` 构造器的第三个参数必须和前面认证实现的名字一致，否则无法找到对应的认证实现类.



### 授权 (Authorization)

* 实现 `UserPermissionDaoStub` 接口的 `findPermissionsByLoginName(String loginName)` 方法，通过用户登录名获取其所以的权限对象。
具体实现可以用任何您想要的方式实现，可以用表连接查询，也可以用视图来实现，或者通过缓存来获取。
  
> 上面接口的实现所关联的实体类需要实现 `PermissionEntityStub` 接口

* 修改上面的 `ShiroFilterChainDefinition` 的定义，按照 Shiro 的规范给 URL 加上所需的权限控制，例如：


```java
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/order/**", "authc, perms[order]");
        chainDefinition.addPathDefinition("/**", "anon");
        return chainDefinition;
    }
```


### 密码处理

默认使用 `DefaultPasswordManager` 来创建密码, 它采用 md5 算法, 
* 如果调用参数为 `UserNamePasswordToken` 的方法创建密码，那么会取用 `UserNamePasswordToken.host` 作为加盐值。
* 否则可以调参数为两个 `String` 的方法，自己指定加盐值。

为了更多的安全，可以实现自己的密码处理, 例如通过`host`映射获取加盐值、外部配置化加盐值，或者给不同的`host`不同的加盐, 那么自己实现接口 `PasswordManager` 并加载这个Bean，那么会自动取代默认的实现.
