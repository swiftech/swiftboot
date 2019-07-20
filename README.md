# SwiftBoot

基于Spring Framework，Spring Boot，Spring MVC，Spring Data 的企业Web应用快速开发框架。

### 为什么要有 SwiftBoot
* 虽然用 Spring Boot 搭建框架进行开发已经简化了很多配置的工作，但是对于大多数中小型的项目来说来，还是有一些重复性的工作。
* 开源社区中已经有很多快速开发框架，它们功能很多但是太过于臃肿并且侵入性很强。

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


### 快速指南

* 在工程中加入对 SwiftBoot 主模块的依赖

	```xml
	<dependencies>
		<dependency>
			<groupId>com.github.swiftech</groupId>
			<artifactId>swiftboot-web</artifactId>
			<version>1.0.13</version>
		</dependency>
	</dependencies>

	```

* 配置

	```java
	@Configuration
    @ComponentScan(basePackages = {"org.swiftboot.web"})
   	public class MyConfig {
    	...
  	}
	```
	
* 实体类
	继承 `BaseEntity` 或者 `BaseIdEntity`
	
	```java
	  @Entity
      @Table(name = "DEMO_ORDER")
      public class OrderEntity extends BaseEntity {
    	...
      }
	```
	
* Command 对象

	```java
	public class OrderCreateCommand extends BasePopulateCommand<OrderEntity> {
    	...
	}
	```

* 返回对象类定义：

	```java
	public class OrderResult extends BasePopulateResult {
		...
	}
	```

* 控制器
	
	```java
	@RequestMapping(value = "order/create", method = RequestMethod.POST)
	public
	@ResponseBody HttpResponse<OrderCreateResult> orderCreate(
		  @RequestBody @Validated @ApiParam("创建订单参数") OrderCreateCommand command) {
    	OrderEntity p = command.createEntity();
		OrderCreateResult ret;
		...
		return new HttpResponse<>(ret);
	}
	```
	
	