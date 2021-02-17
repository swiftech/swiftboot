# SwiftBoot-Data

数据访问层模块，基于 Spring Data JPA 来实现。

## 依赖
* JDK >= 1.8
* Spring Boot >= 2.1.x.RELEASE
* Spring Data JPA >= 2.1.x.RELEASE
* Hibernate >= 5.4.x.Final


### 特性
* 保存新增实体时自动创建主键 ID，预定义多种不同的 ID 生成策略。
* 保存新增实体时自动设置实体的（包括关联对象）创建时间。
* 保存修改实体时自动设置实体的（包括关联对象）更新时间。
* 定义了实体类基类，包含了必要的 ID 字段和大多数表都需要的字段：
  * 主键：`ID`
  * 创建时间：`CREATE_TIME`
  * 更新时间：`UPDATE_TIME`
  * 是否逻辑删除：`IS_DELETE`

## 引用 jar 包：

Maven:

  ```xml
  <dependency>
    <groupId>com.github.swiftech</groupId>
    <artifactId>swiftboot-data</artifactId>
    <version>2.0.0</version>
  </dependency>
  ```

## 如何使用
#### 配置


```java
@Configuration
@ComponentScan(basePackages = {"org.swiftboot.data"})
@EntityScan(basePackages = {"x.x.x"})
@EnableJpaRepositories(basePackages = {"x.x.x"})
public class MyConfig {
}
```
#### Dao

和 Spring Data JPA 所要求的一样，所有 Dao 接口继承 `CrudRepository` 接口或者其子接口，例如：

  ```java
  public interface OrderDao extends CrudRepository<OrderEntity, String> {
  }
  ```

#### 实体类

SwiftBoot-Data 推荐实体类继承 `BaseIdEntity` 或者其子类 `BaseEntity` 来实现，`BaseIdEntity` 定义了32字节长的主键字段 `ID`，`BaseEntity` 继承 `BaseIdEntity` 并定义了创建时间 `CREATE_TIME`、更新时间 `UPDATE_TIME`、是否逻辑删除 `IS_DELETE` 三个字段，SwiftBoot-Data 会在恰当的时刻自动化的填充其中的一些字段。示例：

  ```java
  @Entity
  @Table(name = "DEMO_ORDER")
  public class OrderEntity extends BaseEntity {
      @Column(name = "ORDER_CODE", length = 16, columnDefinition = "COMMENT '订单编号'")
      private String orderCode;

      @Column(name = "DESCRIPTION", length = 64, columnDefinition = "COMMENT '订单描述'")
      private String description;

      @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order", orphanRemoval = true)
      private List<OrderDetailEntity> details;
  }
  ```

> 如果不想通过继承 `BaseIdEntity` 或者其子类 `BaseEntity` 来实现你的实体类继承体系（比如不一样的长度的主键 ID，或者用 `LocalDataTime` 来定义 `CREATE_TIME` ），那么可以通过实现接口 `IdPojo` 来获得 SwiftBoot-Data 的支持（但是部分功能可能无效）

##### 主键ID

* 自动创建主键 ID

  ```yaml
  swiftboot:
    web:
      model:
        autoGenerateId: true
  ```

添加以上配置后，当调用 `Repository` 的 `save()` 或 `saveAll()` 方法保存实体对象的时候，会给实体类自动生成 UUID（默认）类型的主键并填充（包括它的子集合中每个实体的主键 ID 也会生成和填充）。

另外，SwiftBoot-Data 提供了一个更好的主键ID生成器 `EntityIdGenerator`，它可以生成格式为 **业务代码+时间戳+随机字符串** 的长度为32字节的主键ID，例如：`order20190422170606462gbxudaaxgt`，这个主键既有UUID的优点但是比UUID更容易识别并且带来更好的性能。启用这个 ID 生成器只要配置：

  ```java
  @Bean
  IdGenerator idGenerator() {
      return new EntityIdGenerator();
  }
  ```

你也可以使用 snowflake 类型的主键生成器 `SnowflakeIdGenerator`，配置：
  ```java
  @Bean
  IdGenerator idGenerator() {
      return new SnowflakeIdGenerator();
  }
  ```

* 编码实现  
  当然也可以将 `autoGenerateId` 设置为 `false`，并手动编码实现创建 ID 并赋值给实体类，例如：

  ```java
  entity.setId(IdUtils.makeUUID()); // 生成 UUID 主键
  ```

##### 自动设置创建时间和更新时间

* 自动设置功能要求实体类必须实现 `Persistent` 接口。
* 创建时间会在保存新实体之前自动设置。
* 更新时间需要选择自动设置的策略，默认为 `on-change`:
  ```yaml
  swiftboot:
    web:
      model:
        autoUpdateTimeStrategy: on-change
  ```
  * `not-set` 不自动设置更新时间。
  * `on-change` 当有数据变化时自动设置更新时间。
  * `always` 忽略是否有数据变化，强制在保存的时候设置更新时间。
* 一对一或一对多关联的实体或者子实体集合，也会按照以上的规则进行自动设置。