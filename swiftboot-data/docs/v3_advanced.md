# SwiftBoot-Data 高级

## 实体类定义

### 主键生成


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


### 自动生成创建时间和更新时间

如果需要自动生成和保存创建时间和更新时间，根据你期望的时间字段类型，可以分别选择继承相应的抽象基类，他们预先定义了不同类型的创建时间 `CREATE_TIME` 字段和更新时间 `UPDATE_TIME` 字段，具体列表如下：

示例：

  ```java
  @Entity
  @Table(name = "DEMO_ORDER")
  public class OrderEntity extends BaseLocalDateTimeEntity {
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order", orphanRemoval = true)
    private List<OrderDetailEntity> details;
  }
  ```


规则：
* 根据需要的时间类型选择继承哪一个抽象基类。
* 创建时间会在保存新实体之前自动设置。
* 一对一或一对多关联的实体或者子实体集合，也会按照以上的规则进行自动设置。
* 更新时间需要选择自动设置的策略，默认为 `on-change`，参考以下设置：
设置：
  ```yaml
  swiftboot:
    data:
      model:
        autoUpdateTimeStrategy: on-change
  ```
自动设置时间有三种模式：
* `not-set` 不自动设置更新时间。
* `on-change` 当有数据变化时自动设置更新时间。
* `always` 忽略是否有数据变化，强制在保存的时候设置更新时间。


可选的基类：

|实体基类|时间类型|字段类型
| --- | --- | --- |
|BaseLongTimeEntity|java.lang.Long|bigint|
|BaseDateTimeEntity|java.util.Date|datetime|
|BaseLocalDateTimeEntity|java.time.LocalDateTime|datetime|



如果需要选择其他时间类型来定义字段，或者你想给 `createTime`, `updateTime` 绑定不同的数据库字段名称，
那么可以自行创建一个抽象基类并实现接口 `TimePersistable<T>` ，指定你需要的时间类型和数据库字段名称。 （注意要实现 `equals` 和 `hashCode` 方法）
实现了 `TimePersistable<T>` 的子类的实体类的 `createTime`, `updateTime` 字段会分别在创建和修改的时候自动填充当前的时间。


### 逻辑删除

通常在表中添加一个逻辑删除的字段来标记这条记录是不是已经被逻辑删除了，SwiftBoot-Data 对此提供了一些基本的支持。

首先选择标记逻辑删除的字段的类型，可以选择 `Boolean` 或者 `Integer`，根据选择决定相关的类需要继承不同的父类。 例如，选择 `Boolean` 类型的字段记录逻辑删除：

* Entity 类

  继承 `BaseBoolDeleteEntity` 类（其中已经定义了一个 `Boolean` 类型的 `IS_DELETE`（`isDelete`） 字段）例如：

  ```java
  @Entity
  @Table(name = "MY_TABLE")
  public class MyLogicalDeleteEntity extends BaseBoolDeleteEntity {
  }
  ```

  > 如果你的实体类需要继承其他类，而不能继承 `BaseBoolDeleteEntity` 类，可以通过实现 `LogicalDeletePersistable<Boolean>` 接口来自己实现这个字段的定义

* Repository 类

  继承 `BooleanLogicalDeleteExtend` 接口，它已经实现了逻辑删除的 JPA 方法 `deleteLogically` 和 `undeleteLogically`，直接使用即可：

```java
public interface BoolLogicalDeleteDao extends CrudRepository<MyLogicalDeleteEntity, String>,
        BooleanLogicalDeleteExtend<MyLogicalDeleteEntity> {
}
```

> 如果选择 `Integer` 类型，则分别继承 `BaseIntDeleteEntity`(或实现 `LogicalDeletePersistable<Integer>` 接口) 和实现 `IntegerLogicalDeleteExtend` 接口


### 拦截器

由于 SwiftBoot-Data 的 `InterceptorProxy` 注册成了 Hibernate 的拦截器，那么如果你需要注册自定义拦截器就不能使用 Hibernate 的做法了(属性 `hibernate.session_factory.interceptor` 或 `hibernate.ejb.interceptor`)，不然会产生冲突，但是 SwiftBoot-Data
提供了 `InterceptorLoader` 可以在不改变 Hibernate 的拦截器的实现而加载你自己定义的拦截器。也就是说如果你已经实现了 Hibernate 的拦截器，无需做改变即可使用，
只是注册的方法改为使用 `InterceptorRegisterBean` 来注册拦截器，例如：

```java
@Bean
MyHibernateInterceptor myHibernateInterceptor() {
    return new MyHibernateInterceptor(); // 自定义的拦截器
}

@Bean
InterceptorRegisterBean<MyHibernateInterceptor> registerDataPermissionInterceptor() {
    InterceptorRegisterBean<MyHibernateInterceptor> regBean = new InterceptorRegisterBean<>();
    regBean.setOrder(100);
    regBean.setInterceptorName("My Data Interceptor");
    regBean.setInterceptor(myHibernateInterceptor());
    return regBean;
}
```