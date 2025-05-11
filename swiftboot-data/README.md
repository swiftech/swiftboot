# SwiftBoot-Data

数据访问层模块，基于 Spring Data JPA 来实现。


### 特性
* 定义了实体类抽象基类，包含了必要的 `ID` 字段和常见的字段，例如 `CREATE_TIME`, `UPDATE_TIME` 等等
* 保存新增实体时自动创建主键 ID，预定义多种不同的 ID 生成策略，也可以自定义ID生成器。
* 保存新增实体时自动设置实体的（包括关联对象）创建时间。
* 保存修改实体时自动设置实体的（包括关联对象）更新时间。


## 版本:
稳定版：
[v2.md](docs/v2.md)

  ```xml
  <dependency>
  <groupId>com.github.swiftech</groupId>
  <artifactId>swiftboot-data</artifactId>
  <version>2.4.10</version>
</dependency>
  ```

不稳定版:
[v3.md](docs/v3.md)

  ```xml
  <dependency>
    <groupId>com.github.swiftech</groupId>
    <artifactId>swiftboot-data</artifactId>
    <version>3.0.0</version>
  </dependency>
  ```
