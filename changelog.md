2.0.0 (2021-02)
更新内容：
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