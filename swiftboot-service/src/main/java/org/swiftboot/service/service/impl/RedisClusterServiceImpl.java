package org.swiftboot.service.service.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.swiftboot.service.service.RedisService;
import org.swiftboot.service.util.SerializeUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis service implementation for cluster server.
 *
 * @author swiftech 2019-06-11
 * @deprecated
 **/
public class RedisClusterServiceImpl implements RedisService {

    @Resource
    private JedisCluster redisCluster;

    @PostConstruct
    public void init() {
    }

    @Override
    public Jedis getJedis() {
        throw new UnsupportedOperationException("集群不支持此操作");
    }

    @Override
    public boolean setex(String key, int seconds, String value) {
        return "OK".equals(redisCluster.setex(key, seconds, value));
    }

    @Override
    public Long lpush(String key, String value) {
        return redisCluster.lpush(key, value);
    }

    @Override
    public Long rpush(String key, String value) {
        return redisCluster.rpush(key, value);
    }

    @Override
    public Long rpush(String key, String[] value) {
        return redisCluster.rpush(key, value);
    }

    @Override
    public String lpop(String key) {
        return redisCluster.lpop(key);
    }
    @Override
    public String rpop(String key) {
        return redisCluster.rpop(key);
    }

    @Override
    public List<String> brpop(String key, int second) {
        return redisCluster.brpop(second, key);
    }

    @Override
    public Long setnx(String key, String value) {
        return redisCluster.setnx(key, value);
    }

    @Override
    public boolean setex(byte[] key, int seconds, byte[] value) {
        return "OK".equals(redisCluster.setex(key, seconds, value));
    }

    @Override
    public boolean set(String key, String value) {
        return "OK".equals(redisCluster.set(key, value));
    }

    @Override
    public Long decr(String key) {
        return redisCluster.decr(key);
    }

    @Override
    public Long incr(String key) {
        return redisCluster.incr(key);
    }

    @Override
    public Long incrBy(String key, long i) {
        return redisCluster.incrBy(key, i);
    }

    @Override
    public String get(String key) {
        return redisCluster.get(key);
    }


    @Override
    public Long llen(String key) {
        return redisCluster.llen(key);
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        return redisCluster.lrange(key, start, end);
    }

    @Override
    public String lindex(String key, long index) {
        return redisCluster.lindex(key, index);
    }

    @Override
    public void del(String key) {
        redisCluster.del(key);
    }

    @Override
    public void append(String key, String value) {
        redisCluster.append(key, value);
    }

    @Override
    public void append(byte[] key, byte[] value) {
        redisCluster.append(key, value);
    }

    @Override
    public String hget(String key, String field) {
        return redisCluster.hget(key, field);
    }

    @Override
    public Long hset(String key, String field, String value) {
        return redisCluster.hset(key, field, value);
    }

    @Override
    public void hdel(String key, String field) {
        redisCluster.hdel(key, field);
    }

    @Override
    public Set<String> hkeys(String key) {
        return redisCluster.hkeys(key);
    }

    @Override
    public boolean hmset(String key, Map<String, String> hash) {
        return "OK".equals(redisCluster.hmset(key, hash));
    }

    @Override
    public byte[] hget(byte[] key, byte[] field) {
        return redisCluster.hget(key, field);
    }

    @Override
    public Long zadd(String key, double score, String member) {
        return redisCluster.zadd(key, score, member);
    }

    @Override
    public Double zincrby(String key, double score, String member) {
        return redisCluster.zincrby(key, score, member);
    }

    @Override
    public Double zscore(String key, String element) {
        return redisCluster.zscore(key, element);
    }

    @Override
    public Long zcard(String key) {
        return redisCluster.zcard(key);
    }

    @Override
    public Long scard(String key) {
        return redisCluster.scard(key);
    }

    @Override
    public Long zrank(String key, String element) {
        return redisCluster.zrank(key, element);
    }

    @Override
    public Long expire(String key, int seconds) {
        return redisCluster.expire(key, seconds);
    }

    @Override
    public Boolean exists(String key) {
        return redisCluster.exists(key);
    }

    @Override
    public Long ttl(String key) {
        return redisCluster.ttl(key);
    }

    @Override
    public List<String> zrevrange(String key, long start, long end) {
        return redisCluster.zrevrange(key, start, end);
    }

    @Override
    public Long sadd(String key, String member) {
        return redisCluster.sadd(key, member);
    }

    @Override
    public Long srem(String key, String member) {
        return redisCluster.srem(key, member);
    }

    @Override
    public Long zrem(String key, String member) {
        return redisCluster.zrem(key, member);
    }

    @Override
    public Set<String> smembers(String key) {
        return redisCluster.smembers(key);
    }

    @Override
    public String spop(String key) {
        return redisCluster.spop(key);
    }

    @Override
    public String srandmember(String key) {
        return redisCluster.srandmember(key);
    }

    @Override
    public boolean sismember(String key, String member) {
        return redisCluster.sismember(key, member);
    }

    @Override
    public Boolean setnxEx(String key, String value, int seconds) {
        throw new NotImplementedException("setnxEx");
    }

    @Override
    public Long hlen(String key) {
        return redisCluster.hlen(key);
    }

    @Override
    public Object getObject(String key) {
        return SerializeUtils.deserialize(redisCluster.get(key.getBytes()));
    }

    @Override
    public boolean setObject(String key, int seconds, Object object) {
        return this.setex(key.getBytes(), seconds, SerializeUtils.serialize(object));
    }

    @Override
    public String getSet(String key, String value) {
        return redisCluster.getSet(key, value);
    }
}
