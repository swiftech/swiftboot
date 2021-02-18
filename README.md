# SwiftBoot

基于 Spring Boot，Spring MVC，Spring Data 的企业Web应用快速开发框架。

### 为什么要有 SwiftBoot

* 虽然用 SpringBoot 搭建框架进行开发已经简化了很多配置的工作，但是对于大多数中小型的项目来说来，还是有一些重复性的工作。
* 开源社区中已经有很多快速开发框架，它们功能很多但是太过于臃肿并且侵入性很强。
* SwiftBoot 采用一种折中的理念，它并不会覆盖到开发的方方面面，但是它提供必要的和常见的功能支持，以减少重复性的工作和提高开发质量。

### 特性

* 开箱即用，只需要很少的配置即可使用，用更少的代码完成更多的工作。
* 无侵入性，你可以只使用其中的一部分功能，也可以随时去掉它或者切换为别的框架。
* 统一的接口格式（JSON）和错误代码，默认采用前后端分离。
* 接口的返回值对象，实体对象自动填充。
* 自动验证输入参数并自动转换为统一的返回格式。
* 主键UUID自动生成机制。
* 企业 Web 开发常用的工具类和集合类。
* 实现了常见的服务（例如 Redis服务，用户认证等等），减少不必要的重复开发工作，关注核心业务逻辑。
* 实现了基本的用户认证（基于 Redis），可用于 Web 和移动应用的用户认证。
* 常见文档格式转换（开发中）。

### 模块

##### 基础模块:

* [swiftboot-web](swiftboot-web): 企业Web应用基础框架
* [swiftboot-collections](swiftboot-collections): 企业应用集合类
* [swiftboot-utils](swiftboot-utils): 企业应用工具类
  
##### 扩展模块:

* [swiftboot-data](swiftboot-data): 数据访问层模块
* [swiftboot-service](swiftboot-service): 企业应用基础服务
* [swiftboot-auth](swiftboot-auth): 用户认证服务
* [swiftboot-shiro](swiftboot-shiro): 集成 Shiro 用户认证和鉴权
* [swiftboot-sheet](swiftboot-sheet): 表格数据模版化导入导出（支持 Excel, CSV)
* [swiftboot-fileconvert](swiftboot-fileconvert): 文档格式转换（开发中 development）

##### Demo:

* [swiftboot-demo](swiftboot-demo)：演示项目，可以作为脚手架快速搭建新项目


### 版本

* 稳定版：v2.0.0 (2021-02)
  更新内容：
  * `swiftboot-web` 控制器参数自动填充功能支持编辑子对象的填充。
  * 将数据访问层从 `swiftboot-web` 独立出来成为 `swiftboot-data` 模块，方便用于任何 JPA 应用程序使用。
  * `swiftboot-data` 重新实现自动设置创建时间和更新时间，支持一对一关联和一对多关联实体的自动设置更新时间。
  * `swiftboot-data` 新增 snowflake ID 生成器。
  * 新增 `swiftboot-sheet` 模块提供了模版化的表格数据导出和导入的功能，支持 Excel 和 CSV。
  * 依赖库升级和bug修复。


[完整更新记录](changelog.md)

### 概览

* 从接口的 Command 对象创建 Entity 实例并自动填充传递进来的值：

  ```java
  OrderEntity p = orderCommand.createEntity();
  ```

  > 一对一或一对多关联的实体类（集合）也会被自动创建和填充

* 从 Entity 实例创建返回值对象并自动填充值：

  ```java
  OrderResult r = OrderResult.createResult(OrderResult.class, orderEntity);
  ```

  > 一对一或一对多关联的实体类（集合）也会自动填充返回值对象的关联类（集合）

* 全局唯一 ID 生成器，长度 32 字节，比 UUID 有更好的插入性能且易于识别出所对应的业务对象以及创建时间，例如：
    ```
    order20191231000850307uxplsioasi
    ```

  > 创建 Entity 实例时无需调用生成器生成 ID，SwiftBoot 会自动创建并填充。

* 认证和授权：
    * 如果仅需要认证机制，那么集成 [swiftboot-auth](swiftboot-auth) 模块
    * 如果不仅需要认证，还需要授权，那么集成 [swiftboot-shiro](swiftboot-shiro) 模块

  > 两个模块不能同时启用，且他们的认证实现机制不同