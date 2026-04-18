# Changelogs

## V3.1.1
* 增加接口限流注解 `RateLimit` 用于给特定接口限流，或者给特定接口针对用户进行限流。
* 优化了多语言资源文件的使用方式，开发者不需要再自己的项目中显示声明 SwiftBoot-Web 的资源文件位置了，SwiftBoot 会自动处理。
* 实现了从 Swagger 注解中获取多语言化的资源用来显示参数验证错误信息。


## V3.1
* 重构和优化

## V3.0.0
* 错误消息增加多语言的支持
* 支持 Java 8 时间类型的数据格式转换，包括 `YearMonth` 和 `MonthDay`。
* 用接口 `PopulatableDto` 取代抽象类 `BasePopulateDto` 来实现参数填充，DTO无需继承类使得实现动态填充更加灵活，并且增加 `includRelation` 来显式的决定是否要填充子对象。
* 支持 Swagger V3
* 命名风格：用 `Request` 类名后缀替换 `Command` 后缀, 用 `DTO` 类名后缀替换 `Result` 后缀
* `HttpReponse` 改名为 `Reponse`，变量名 `msg` 改为 `message`、`result` 改为 `data`