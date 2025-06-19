# SwiftBoot

A lightweight rapid development framework based on Spring Boot, Spring MVC and Spring Data for enterprise.

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
* 提供基于主流安全框架的用户认证和授权。
* 表格数据模版化导入导出，支持 Excel, CSV。
* 企业 Web 开发常用的工具类和集合类。

### 版本

##### 稳定版: v2.4.10
基于SpringBoot 2.7，JDK 8
[v2.md](docs/v2.md)


##### 不稳定版: v3.0.0
基于SpringBoot 3.4+，JDK 17+
[v3.md](docs/v3.md)


[完整更新记录](changelogs.md)
