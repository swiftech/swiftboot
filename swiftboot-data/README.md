# SwiftBoot-Data

数据访问层模块，基于 Spring Data JPA 来实现。

## 依赖
* JDK >= 1.8
* Spring Boot >= 2.2.x.RELEASE
* Spring Data JPA >= 2.2.x.RELEASE
* Hibernate >= 5.4.x.Final


### 特性
* 定义了实体类抽象基类，包含了必要的 `ID` 字段和常见的字段，例如 `CREATE_TIME`, `UPDATE_TIME` 等等
* 保存新增实体时自动创建主键 ID，预定义多种不同的 ID 生成策略，也可以自定义ID生成器。
* 保存新增实体时自动设置实体的（包括关联对象）创建时间。
* 保存修改实体时自动设置实体的（包括关联对象）更新时间。


## Maven:

  ```xml
  <dependency>
    <groupId>com.github.swiftech</groupId>
    <artifactId>swiftboot-data</artifactId>
    <version>2.0.2</version>
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

和 Spring Data JPA 所要求的一样，所有 Dao 接口继承 `org.springframework.data.repository.Repository` 接口或者其子接口，例如：

  ```java
  public interface OrderDao extends CrudRepository<OrderEntity, String> {
  }
  ```

#### 实体类


为了获得 SwiftBoot-Data 提供的自动创建 ID 的功能，需要你的实体类实现 `IdPersistable` 接口。
如果需要自动填充和保存创建时间和更新时间，实现接口 `TimePersistable<T>`（注意要实现 `equals` 和 `hashCode` 方法）
更简单的做法是，让实体类继承抽象的 `BaseIdEntity` 或者其子类。`BaseIdEntity` 定义了 32 字节长的主键字段 `ID`，
对于创建时间和更新时间，根据你期望的时间字段类型，可以分别选择继承相应的抽象基类，他们预先定义了不同类型的创建时间 `CREATE_TIME` 字段和更新时间 `UPDATE_TIME` 字段，具体列表如下：

|实体基类|时间类型|字段类型
| --- | --- | --- |
|BaseLongTimeEntity|java.lang.Long|bigint|
|BaseDateTimeEntity|java.util.Date|datetime|
|BaseLocalDateTimeEntity|java.time.LocalDateTime|datetime|

> 如果需要选择其他时间类型来定义字段，自行创建一个抽象基类并实现接口 `TimePersistable<T>` ，指定你需要的时间类型。

示例：

  ```java
  @Entity
  @Table(name = "DEMO_ORDER")
  public class OrderEntity extends BaseLocalDateTimeEntity {
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order", orphanRemoval = true)
    private List<OrderDetailEntity> details;
  }
  ```


###### 主键ID

SwiftBoot-Data 可以配置为自动化的填充实体类预定义的 `ID`（所有实现 `IdPersistable` 接口的实体）。关联的实体对象和子集合中的实体对象的 `ID` 也会被自动的填充。

* 开启自动创建主键 `ID`


  ```yaml
  swiftboot:
    data:
      model:
        autoGenerateId: true
  ```

添加以上配置后，当调用 `Repository` 的 `save()` 或 `saveAll()` 方法保存实体对象的时候，会给实体类自动生成 UUID（默认）类型的主键并填充（包括它的子集合中每个实体的主键 ID 也会生成和填充）。


* 主键生成器
SwiftBoot-Data 提供了一个比 UUID 更好的主键 ID 生成器 `EntityIdGenerator`，它可以生成格式为 **业务代码+时间戳+随机字符串** 的长度为32字节的主键ID，例如：`order20190422170606462gbxudaaxgt`，这个主键既有 UUID 的优点但是比 UUID 更容易识别并且带来更好的性能。启用这个 ID 生成器只要配置：


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
  也可以将 `autoGenerateId` 设置为 `false`，并手动编码实现创建 ID 并赋值给实体类，例如：


  ```java
  entity.setId(IdUtils.makeUUID()); // 生成 UUID 主键
  ```

###### 自动设置创建时间和更新时间
继承了 `TimePersistable` 的子类的实体类的 `CREATE_TIME`, `UPDATE_TIME` 字段会分别在创建和修改的时候自动填充当前的时间。
  
* 根据需要的时间类型选择继承哪一个抽象基类。
* 自定义实现 `TimePersistable` 接口的实体类不会被自动填充。
* 创建时间会在保存新实体之前自动设置。
* 更新时间需要选择自动设置的策略，默认为 `on-change`:
  

  例如：
  ```yaml
  swiftboot:
    data:
      model:
        autoUpdateTimeStrategy: on-change
  ```

  * `not-set` 不自动设置更新时间。
  * `on-change` 当有数据变化时自动设置更新时间。
  * `always` 忽略是否有数据变化，强制在保存的时候设置更新时间。

* 一对一或一对多关联的实体或者子实体集合，也会按照以上的规则进行自动设置。