# SwiftBoot-shiro （开发中）

SwiftBoot-shiro 封装了 Apache Shiro 实现了认证（Authentication）和授权（Authorization）

配置好了 Shiro 相关的类, 并且实现了一个通用的 Realm 类, 它可以同时处理不同的帐号体系的认证和授权.

---

# 使用

### 依赖
* redis


### 常规
* 定义用户（认证）实体类，实现 `UserEntityStub` 接口，


### 认证
* 实现 `UserAuthDaoStub` 接口，这个实现会最终交由处理最终实际的用户数据的查询（因为支持多种认证机制同时工作）
```
@Component("my-auth-service-name")
public class DemoUserAuthenticator implements UserAuthStub<DemoUserEntity> {

}
```
注意 `@Component` 注解的名字

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
注意 `UserNamePasswordToken` 第三个参数必须和前面认证实现的名字一致，否则无法找到对应的认证类.



### 授权

* 实现 `UserPermissionViewDaoStub` 接口




### 密码处理

默认使用 `DefaultPasswordManager` 来创建密码, 它采用 md5 算法, 
* 如果调用参数为 `UserNamePasswordToken` 的方法创建密码，那么会取用 `UserNamePasswordToken.host` 作为加盐值。
* 否则可以调参数为两个 `String` 的方法，自己指定加盐值。

为了更多的安全，可以实现自己的密码处理, 例如通过`host`映射获取加盐值、外部配置化加盐值，或者给不同的`host`不同的加盐, 那么自己实现接口 `PasswordManager` 并加载这个Bean，那么会自动取代默认的实现.

---

# 开发