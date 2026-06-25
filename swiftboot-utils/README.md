# SwiftBoot-utils 
企业应用开发常用工具类

---

### 功能

* `AnnotationUtils` 通过反射处理 Java 注解的工具类

* `BeanUtils` 通过反射机制操作对象

* `BitUtils` 二进制位操作

* `BooleanUtils` 布尔值工具类

* `BufferedIoUtils` 带有缓存的IO流工具类

* `ClasspathResourceUtils` 处理类路径资源的工具类

* `CryptoUtils` md5加盐

* `EnvUtils` 系统环境变量工具类

* `GenericUtils` 泛型类型工具

* `I18nHelper` 用于管理应用程序区域设置和资源包的国际化辅助类。

* `IdUtils` ID工具类，生成UUID，业务ID，流水号等

* `ImageUtils` 处理图片的工具类

* `IoUtils` IO读写工具类

* `JsonUtils` Json处理工具类，支持 Java8 时间类型

* `NetworkUtils` 与网络相关的工具方法

* `NumberFormatUtils` 数字格式化工具类

* `PasswordUtils` 处理密码创建和验证的工具类，支持加盐

* `PathUtils` 处理文件路径的工具类

* `PropertiesUtils` 处理 .properties 文件的工具类

* `PreferenceManager` Java Preference类的包装器，更加简单和方便的管理偏好设置

* `SysUtils` 系统环境相关的工具类

* `TextUtils` 文本处理工具类

* `UrlUtils` URL相关的工具类

* `VersionUtils` 版本号比较工具类

* `WordUtils` 处理英文单词的工具类

* `ZipUtils` ZIP文件工具类

* 时间相关的工具类
	* `CronUtils` 生成 Spring 的 cron 表达式
	* `ElapsedTime` 用来计算经过的时间（毫秒）
	* `MonthDayUtils` 提供和计算 MonthDay 相关的工具方法
	* `LocalDateTimeUtils` 处理 LocalDateTime 时间类型的工具类

### Maven

稳定版：
[v2.md](docs/v2.md)

	```xml
	<dependency>
	    <groupId>com.github.swiftech</groupId>
	    <artifactId>swiftboot-utils</artifactId>
	    <version>2.4.11</version>
	</dependency>
	```

不稳定版:
[v3.md](docs/v3.md)

  ```xml
  <dependency>
    <groupId>com.github.swiftech</groupId>
    <artifactId>swiftboot-utils</artifactId>
    <version>3.1.5</version>
  </dependency>
  ```
