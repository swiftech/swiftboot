package org.swiftboot.service.service;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis 连接服务，所有服务需要用到 Redis 存储的都通过 getJedis() 方法进行操作
 *
 * @author swiftech
 */
public interface RedisService {

    /**
     * 获取 Jedis 实例进行 Redis 操作
     *
     * @return
     */
    Jedis getJedis();


    /**
     * 设置一个值并指定超时时间（单位秒）,用于存储字符串
     *
     * @param key
     * @param seconds 超时时间（单位秒）
     * @param value
     * @return
     */
    boolean setex(final String key, final int seconds, final String value);

    /**
     * 在名称为key的队列左边推入一个值value
     *
     * @param key
     * @param value
     * @return
     */
    Long lpush(final String key, final String value);

    /**
     * 在名称为key的队列右边推入一个值value
     *
     * @param key
     * @param value
     * @return
     */
    Long rpush(final String key, final String value);

    /**
     * 在名称为key的队列尾添加多个值
     *
     * @param key
     * @param value
     * @return
     */
    Long rpush(final String key, final String[] value);

    /**
     * 从名称为key的队列右边弹出一个值
     *
     * @param key
     * @return
     */
    String lpop(String key);

    /**
     * 从名称为key的队列右边弹出一个值
     *
     * @param key
     * @return
     */
    String rpop(final String key);

    List<String> brpop(final String key, final int second);

    Long setnx(final String key, final String value);

    /**
     * 设置一个名称为 key 值并指定超时时间（单位秒）,用于存储二进制对象
     *
     * @param key
     * @param seconds 超时时间（单位秒）
     * @param value
     * @return
     */
    boolean setex(final byte[] key, final int seconds, final byte[] value);


    /**
     * 设置名称为 key 的值
     *
     * @param key
     * @param value
     * @return
     */
    boolean set(final String key, final String value);

    /**
     * 名称为key的整型数字减1操作
     *
     * @param key
     * @return
     */
    Long decr(final String key);

    /**
     * 名称为key的整型数字增1操作
     *
     * @param key
     * @return
     */
    Long incr(final String key);

    /**
     * 名称为key的整型数字增加 i 操作
     *
     * @param key
     * @param i
     * @return
     */
    Long incrBy(final String key, final long i);

    /**
     *
     * @param key
     * @return
     */
    String get(final String key);

    /**
     * 返回名称为key的list的长度
     *
     * @param key
     * @return Long
     */
    Long llen(final String key);

    /**
     * 返回名称为key的list中start至end之间的元素（下标从0开始）
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    List<String> lrange(final String key, final long start, final long end);

    /**
     * 返回名称为key的list中index位置的元素
     *
     * @param key
     * @param index
     * @return
     */
    String lindex(final String key, final long index);

    void del(final String key);

    void append(final String key, final String value);

    void append(final byte[] key, final byte[] value);

    String hget(final String key, final String field);

    Long hset(final String key, final String field, final String value);

    void hdel(final String key, final String field);

    Set<String> hkeys(final String key);

    boolean hmset(final String key, final Map<String, String> hash);

    byte[] hget(final byte[] key, final byte[] field);

    /**
     * 向名称为key的zset中添加元素member，score用于排序。如果该元素已经存在，则根据score更新该元素的顺序。
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    Long zadd(final String key, final double score, final String member);

    /**
     * 如果在名称为key的zset中已经存在元素member，则该元素的score增加increment；否则向集合中添加该元素，
     * 其score的值为increment
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    Double zincrby(final String key, final double score, final String member);

    /**
     * 返回名称为key的zset的基数
     *
     * @param key
     * @param element
     * @return
     */
    Double zscore(final String key, final String element);

    /**
     * 返回有序集 key 的基数。
     *
     * @param key
     * @return
     */
    Long zcard(final String key);

    /**
     * 返回有序集 key 的基数。
     *
     * @param key
     * @return
     */
    Long scard(final String key);

    /**
     * 返回有序集key中成员member的排名。其中有序集成员按score值递增(从小到大)顺序排列。
     * 排名以0为底，也就是说score值最小的成员排名为0。
     */
    Long zrank(final String key, final String element);


    /**
     * 为名称为key的值设置失效时间
     *
     * @param key
     * @param seconds
     * @return
     */
    Long expire(final String key, final int seconds);

    /**
     * 确认一个key是否存在
     *
     * @param key
     * @return
     */
    Boolean exists(final String key);

    /**
     * @param key
     * @return
     */
    Long ttl(final String key);

    /**
     * 返回名称为key的zset（元素已按score从大到小排序）中的index从start到end的所有元素,index从0开始
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    Set<String> zrevrange(final String key, final long start, final long end);

    /**
     * 向名称为key的set中添加元素member
     *
     * @param key
     * @param member
     * @return
     */
    Long sadd(final String key, final String member);

    Long srem(final String key, final String member);

    Long zrem(final String key, final String member);

    /**
     * 返回名称为key的set的所有元素
     *
     * @param key
     * @return
     */
    Set<String> smembers(final String key);

    /**
     * 移除并返回集合中的一个随机元素。
     */
    String spop(final String key);

    /**
     * @param key
     * 返回集合中的一个随机元素。
     */
    String srandmember(final String key);

    /**
     * 判断 member 元素是否集合 key 的成员。
     *
     * @param key
     * @param member
     * @return
     */
    boolean sismember(final String key, final String member);

    Boolean setnxEx(final String key, final String value, final int seconds);

    /**
     * 返回map集合长度
     *
     * @param key
     * @return
     */
    Long hlen(final String key);

    /**
     * 获取名为 key 的反序列化后的对象
     * @param key
     * @return
     */
    Object getObject(String key);

    /**
     * 序列化 value 并设置名为 key 的值
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    boolean setObject(String key, int seconds, Object value);

    String getSet(final String key, final String value);

}
