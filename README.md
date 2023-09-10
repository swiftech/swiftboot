# SwiftBoot

A lightweight rapid development framework based on Spring Boot, Spring MVC and Spring Data for enterprise

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
* 主键ID自动生成，字段自动填充机制。
* 提供常见的服务（例如 Redis服务，用户认证等等），减少不必要的重复开发工作，关注核心业务逻辑。
* 提供基本的用户认证（基于 Redis），可用于 Web 和移动应用的用户认证。
* 提供基于 Shiro 框架的用户认证和授权，简化 Shiro 框架的使用。
* 表格数据模版化导入导出，支持 Excel, CSV。
* 企业 Web 开发常用的工具类和集合类。

### 模块

##### 基础模块:

* [swiftboot-web](swiftboot-web): 企业Web应用基础框架
* [swiftboot-data](swiftboot-data): 数据访问层模块
* [swiftboot-collections](swiftboot-collections): 企业应用集合类
* [swiftboot-utils](swiftboot-utils): 企业应用工具类

##### 扩展模块:

* [swiftboot-service](swiftboot-service): 企业应用基础服务
* [swiftboot-auth](swiftboot-auth): 用户认证服务
* [swiftboot-shiro](swiftboot-shiro): 集成 Shiro 用户认证和鉴权
* [swiftboot-sheet](swiftboot-sheet): 表格数据模版化导入导出（支持 Excel, CSV)
* [swiftboot-fileconvert](swiftboot-fileconvert): 文档格式转换（开发中 development）

##### Demo:

* [swiftboot-demo](swiftboot-demo)：演示项目，可以作为脚手架快速搭建新项目


### 版本

##### Stable: v2.3.1

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

##### Unstable: v2.4.3

* `[sheet]`: Fix exception when selecting sheet by name.
* `[sheet]`: Fix exception when reading cells with blank cell ahead.
* update spring boot and spring data to 2.7.15

[完整更新记录](changelog.md)

### 概览

* 入参自动填充
  * 从接口的 Command 对象创建 Entity 实例并自动填充传递进来的值：


  ```java
  OrderEntity p = orderCommand.createEntity();
  orderRepository.save(p);
  ```
  * 编辑实体对象时调用 `populateEntity()` 方法，此实体类一对一、一对多关联的实体类（集合）也会被自动创建和填充 ：


  ```java
  orderEntity.getDetails().clear();
  orderCommand.populateEntity(orderEntity);
  orderRepository.save(p);
  ```

> 一对一，一对多关联的实体类（集合）也会被自动创建和填充
> 详细参考： [swiftboot-web](swiftboot-web)

* 从 Entity 实例创建返回值对象并自动填充值：


  ```java
  OrderResult r = OrderResult.createResult(OrderResult.class, orderEntity);
  ```

  > 一对一或一对多关联的实体类（集合）也会自动填充返回值对象的关联类（集合）
  > 详细参考： [swiftboot-web](swiftboot-web)

* 字段自动填充 
  * ID 自动填充，创建 Entity 实例时无需调用生成器生成 ID，SwiftBoot 会自动创建并填充。 
  * 全局 ID 生成器，生成如 `order20191231000850307uxplsioasi` 的长度为 32 字节的全局ID，比 UUID 有更好的插入性能且易于识别出所对应的业务对象以及创建时间。
  * 保存实体类时创建时间和修改时间自动填充。

> 详细参考： [swiftboot-data](swiftboot-data)

* 认证和授权：
    * 如果仅需要认证机制，那么集成 [swiftboot-auth](swiftboot-auth) 模块
    * 如果不仅需要认证，还需要授权，那么集成 [swiftboot-shiro](swiftboot-shiro) 模块

  > 两个模块不能同时启用，且他们的认证实现机制不同