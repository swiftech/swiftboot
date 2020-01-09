# SwiftBoot

基于Spring Framework，Spring Boot，Spring MVC，Spring Data 的企业Web应用快速开发框架。

### 为什么要有 SwiftBoot
* 虽然用 Spring Boot 搭建框架进行开发已经简化了很多配置的工作，但是对于大多数中小型的项目来说来，还是有一些重复性的工作。
* 开源社区中已经有很多快速开发框架，它们功能很多但是太过于臃肿并且侵入性很强。
* SwiftBoot 采用一种折中的理念，它并不会覆盖到开发的方方面面，但是它提供必要的和常见的功能支持，以减少重复性的工作和提高开发质量。

### 特性
* 开箱即用，只需要很少的配置即可使用，用更少的代码完成更多的工作。
* 无侵入性，你可以只使用其中的一部分功能，也可以随时去掉它或者切换为别的框架。
* 统一的 Restful 接口格式（JSON）和错误代码。
* 接口的返回值对象，实体对象自动填充。
* 自动验证输入参数并自动转换为统一的返回格式。
* 主键UUID自动生成机制。
* 企业 Web 开发常用的工具类和集合类。
* 常见文档格式转换（开发中）。


### 模块
* [swiftboot-web](swiftboot-web/): 企业Web应用基础框架
* [swiftboot-collections](swiftboot-collections/): 企业应用集合类
* [swiftboot-utils](swiftboot-utils/): 企业应用工具类
* [swiftboot-rpc](swiftboot-rpc/): Spring Cloud RPC（试验）
* [swiftboot-fileconvert](swiftboot-fileconvert/): 文档格式转换（开发中）
* [swiftboot-demo](swiftboot-demo/)：演示项目，可以作为脚手架快速搭建新项目


### 发布

2019-09-08 1.0.14 release
* 新增了一些工具类
* 升级了依赖的库
* 重构一些代码以及更新文档
* 重构了Demo

[完整记录](changelog.md)

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
