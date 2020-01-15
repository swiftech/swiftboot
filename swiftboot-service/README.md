# swiftboot-service（试验）
提供 Web 开发中常见的服务

### 特性
* Redis 服务，支持集群模式
* Captcha 服务，实现急于文本的防机器人验证码

### Maven

```xml
<dependency>
    <groupId>com.github.swiftech</groupId>
    <artifactId>swiftboot-service</artifactId>
    <version>1.1</version>
</dependency>

```
	
### 使用方法
* application.yaml
```yaml
swiftboot:
  service:
    redis:
      host: localhost
      port: 6379
  captcha:
    expiresIn: 300
```

* MyController.java
```java
public class MyController {
    @Resource
    private RedisService redisService;
    
    @Resource
    private CaptchaService captchaService;

}
```