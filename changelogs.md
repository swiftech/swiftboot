v3.0.0
* New Modules:
  * `[swiftboot-security]` is introduced with Spring Security support.
  * new `swiftboot-common-auth` module to share similar classes between `swiftboot-auth` and `swiftboot-security`.
* Deprecated Modules; 
  * `[swiftboot-shiro]` module is removed, which means Shiro is no longer supported.
* New Features
  * `[swiftboot-web]`: allow `BasePopulateDto` to include or exclude related DTOs during populating from entities.
  * `[swiftboot-web]`: handle DTO automatic populating by new interface `PopulatableDto` instead of class `BasePopulateDto`, so that
  the DTOs would never inherit from a class but an interface, which gives more flexibility. All static methods are moved from `BasePopulateDto` to `PopulatableDto`.

v2.4.10
* fix: potential memory leak from ClasspathResourceUtils
* update dependencies.

v2.4.9
* `[utils]`: new utils methods to join array or list of strings ignoring the null or the blank strings.
* update dependencies.
* 
v2.4.8
* `[collections]`: add `findAncestor()` to Node class
* `[utils]`: add `removeQuotes()` to `TextUtils` class

v2.4.7
* `[utils]` add `getPreferenceAlias()` methods to PreferenceManager for migrating preferences.

v2.4.6
* `[utils]`: fix `PathUtils.isParentFolder` doesn't work well;
* update dependencies.

v2.4.5
* `[utils]`: add new `PathUtils`, `UrlUtils` classes, and new method to `ClasspathResourceUtils`, `SysUtils`, `EnvUtils`, `WordUtils`.
* `[collections]`: remove deprecated classes.
* refactor for logger and update docs.

v2.4.4.1
* `[utils]`: add `forceGetProperties(Object)` and `forceSetProperties(Object, Map)` to `BeanUtils`.
* `[utils]`: add `getPreference(String)` method to `PreferenceManager`

v2.4.3
* `[sheet]`: Fix exception when selecting sheet by name.
* `[sheet]`: Fix exception when reading cells with blank cell ahead.
* update spring boot and spring data to 2.7.15

v2.4.2
* `[sheet]`: Fix exception when read any shapes other than picture.
* dependencies update and doc update. 

v2.4.1
* `[sheet]`: Fix exception when read a null cell.

v2.4.0
* `[sheet]`: Add support for reading images from Excel.
* `[sheet]`: Add support for reading csv/excel data from dynamic position(which is determined by a function).
* `[sheet]`: Add support for reading uncertain rows for Excel(stops when getting first empty row).
* `[sheet]`: Replace SheetHandler/CellHandler interfaces with Consumer.
* `[sheet]`: Fix some bugs of importing csv and Excel files.
* `[sheet]`: Fix bug of reading value of Excel FORMULA cell.
* dependencies update and doc update.

v2.3.1
* `[utils]` Add CalcUtils for calculating.
* `[utils]` Add support for Reader to IoUtils.
* `[utils]` Add readAllAsString() method to BufferedIoUtils.
* `[collections]` Improve the CollectionUtils.toLinkedList() method.
* Dependencies update: 
  * SpringBoot 2.7.12
  * SpringDataJPA 2.7.9
  * Hibernate 5.6.14.Final
  * Jedis 4.3.1
  * Poi 5.2.3
  * JSON 20220320
  * etc.

v2.3
* `[utils]`Add wrapper class PreferenceManager to enhance the management for Java preferences.
* `[service]`Update redis client dependencies and RedisService's implementation;
* `[web]`Add new TextUtils class for advanced indexing in text.
* `[web]`Add ElapsedTime class for calculating time elapsing.
* Dependencies upgrade, includes:
    * SpringBoot 2.6.11
    * SpringDataJPA 2.7.2
    * Hibernate 5.6.11.Final
    * Jedis 4.2.3
    * etc.

v2.2.1
* Dependencies update

v2.2
* `[collection]`add Tree class to collections.
* `[web]`add missing error msg translating for AuthFilter
* `[web]`change the way to implement user authentication.
* `[web]`return user token through http response header instead of response body.
* `[web]`add parameterized error message support to HttpResponse object.
* `[web]`add parameterized error message support for user-defined validators.
* `[web]`refactored the error code support.
* `[web]`re-implement the translating from error code to message by response advice.
* `[web]`phone number validator supports parameterized error message.

v2.1.1
* `[web]` 修复自定义验证器没有加载资源的bug
* `[data]` 增加逻辑删除的 JPA 扩展接口。
* `[data]` 增加 `InterceptorLoader` 用于加载用户自定义的 Hibernate 拦截器
* `[data]` 修复了自动填充 ID 时子对象重复填充的bug。
* `[sheet]` 导出增加多表格支持、复制单元格样式功能、合并单元格功能，修复bug和重构。
* `[web]` 废弃 `WebMessageConverter`，不再需要显式声明，简化使用。
* `[web]` 新增 `HeaderUtils` 和 `SpringWebUtils` 处理 HTTP 头。
* `[auth]` 增加将用户会话信息自动填充至接口参数对象或者变量的功能。
* `[auth]` 增加全部清除用户会话的功能。
* `[auth]` Mock会话服务增加磁盘存储。
* SpringBoot 配置文档化
* 修复了一些bug
* 大量代码重构和文档更新
* SpringBoot 及其他依赖库升级

2.0.2 (2021-03-14)
* `swiftboot-data` 支持自定义 Hibernate 拦截器（通过 `InterceptorLoader` 加载）

2.0.1
* `swiftboot-data` 修复在 save() 之前 Hibernate 发生 flush 时没有自动设置 ID 的问题

2.0.0 (2021-02)
* `swiftboot-web` 控制器参数自动填充功能支持编辑子对象的填充。
* `swiftboot-data` 重新实现自动设置创建时间和更新时间，支持一对一关联和一对多关联实体的自动设置更新时间。

1.3-beta3
* `swiftboot-sheet` 重构并增加文档
* 依赖库升级和bug修复

1.3-beta2 (2021-01-18)
* `swiftboot-data` 新增 snowflake ID 生成器
* 新增 `swiftboot-sheet` 提供了模版化的表格数据导出和导入的功能
* 依赖库升级和bug修复

1.3-beta1 (2020-09-29)
* 将数据访问层从 `swiftboot-web` 独立出来成为 `swiftboot-data` 模块，方便用于任何 JPA 应用程序使用
* 依赖库升级和bug修复

2020-08-25 1.2.1 release
* 修复了 `swiftboot-shiro` 配置加载的bug
* 依赖库升级

2020-06-6 1.2 release
* 新增 `swiftboot-shiro` 模块，通过集成 Shiro 提供用户认证和授权
* 新增浮点数字符串校验 `DecimalString`
* 会话存储可选内存或者 Redis
* 日志输出优化
* 改用 Spring 自带的跨域过滤器
* 依赖库升级
* bug 修复

2020-03-15 1.1.1 release
* bug 修复
* 依赖库升级
* 新增 OnlyNumber 校验
* 重构代码

2020-02-02 1.1 release 
* 新增 `swiftboot-service` 和 `swiftboot-auth` 模块，提供常用的服务和简单的用户认证机制
* 新增输入日志多语言化
* 优化自动填充对象
* 新增一些工具类和工具方法
* 升级依赖的库
* 修复了很多bug
* 完善 demo 项目

2019-09-08 1.0.14 release
* 新增了一些工具类
* 升级了依赖的库
* 重构一些代码以及更新文档
* 重构了Demo

2019-07-20 1.0.13 release
* 第一个稳定版本发布

2018-12-19 1.0-SNAPSHOT
* 初次提交