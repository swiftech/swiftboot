# Changelogs

## V3.0.0
* 错误消息增加多语言的支持
* 支持 Java 8 时间类型的数据格式转换，包括 YearMonth 和 MonthDay。
* 用接口 `PopulatableDto` 取代抽象类 `BasePopulateDto` 来实现参数填充，DTO无需继承类使得实现动态填充更加灵活，并且增加 `includRelation` 来显式的决定是否要填充子对象。
* 支持 Swagger V3
* 命名风格：用 `Request` 类名后缀替换 `Command` 后缀, 用 `DTO` 类名后缀替换 `Result` 后缀
* `HttpReponse` 改名为 `Reponse`，变量名 `msg` 改为 `message`、`result` 改为 `data`