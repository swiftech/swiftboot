# SwiftBoot

基于Spring Framework，Spring Boot，Spring MVC，Spring Data 的企业Web应用快速开发开发框架。

### 模块
* [swiftboot-web](swiftboot-web/README.md): 企业Web应用基础框架
* [swiftboot-collections](swiftboot-collections/README.md): 企业应用集合类
* [swiftboot-utils](swiftboot-utils/README.md): 企业应用工具类
* [swiftboot-demo](swiftboot-demo/README.md)：演示项目，可以作为脚手架快速搭建新项目




### 快速指南

* pom.xml

	```xml
	<dependencies>
		<dependency>
			<groupId>com.github.swiftech</groupId>
			<artifactId>swiftboot-web</artifactId>
			<version>1.0.6-SNAPSHOT</version>
		</dependency>
	</dependencies>

	```
	
	```xml
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.github.swiftech</groupId>
                <artifactId>swiftboot</artifactId>
                <version>1.0.6-SNAPSHOT</version>
            </dependency>
        </dependencies>
    </dependencyManagement>
	```



