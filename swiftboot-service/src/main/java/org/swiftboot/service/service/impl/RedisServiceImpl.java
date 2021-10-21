
package org.swiftboot.service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.service.config.SwiftbootServiceConfigBean;
import org.swiftboot.service.service.RedisService;
import org.swiftboot.service.util.SerializeUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis service implementation for standalone server.
 *
 * @author swiftech
 */
public class RedisServiceImpl implements RedisService {

    private final Logger log = LoggerFactory.getLogger(RedisServiceImpl.class);

    private JedisPool jedisPool;

    private String redisHost = "localhost";

    private int redisPort = 6379;

    @Resource
    SwiftbootServiceConfigBean swiftbootServiceConfigBean;


    @PostConstruct
    public void init() {
        redisHost = swiftbootServiceConfigBean.getRedis().getHost();
        redisPort = swiftbootServiceConfigBean.getRedis().getPort();
        jedisPool = new JedisPool(redisHost, redisPort == 0 ? 6379 : redisPort);
        log.info(String.format("Connected to Redis Server -> %s:%d", redisHost, redisPort));
    }

    @PreDestroy
    public void destroy() {
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis != null && jedis.isConnected()) {
                jedis.disconnect();
            }
        }
        jedisPool.destroy();
    }

    @Override
    public Jedis getJedis() {
//        log.debug("Redis当前连接数：" + jedisPool.getNumActive());
        return jedisPool.getResource();
    }

    public boolean setex(final String key, final int seconds, final String value) {
        try (Jedis jedis = getJedis()) {
            return "OK".equals(jedis.setex(key, seconds, value));
        } catch (Exception e) {
            return false;
        }
    }

    public Long lpush(final String key, final String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.lpush(key, value);
        } catch (Exception e) {
            return 0L;
        }
    }

    public Long rpush(final String key, final String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.rpush(key, value);
        } catch (Exception e) {
            return 0L;
        }
    }

    public Long rpush(final String key, final String[] value) {
        try (Jedis jedis = getJedis()) {
            return jedis.rpush(key, value);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public String lpop(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.lpop(key);
        } catch (Exception e) {
            return null;
        }
    }

    public String rpop(final String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.rpop(key);
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> brpop(final String key, final int second) {
        try (Jedis jedis = getJedis()) {
            return jedis.brpop(second, key);
        } catch (Exception e) {
            return null;
        }
    }

    public Long setnx(final String key, final String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.setnx(key, value);
        } catch (Exception e) {
            return 0L;
        }
    }


    public boolean setex(final byte[] key, final int seconds, final byte[] value) {
        try (Jedis jedis = getJedis()) {
            return "OK".equals(jedis.setex(key, seconds, value));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean set(final String key, final String value) {
        try (Jedis jedis = getJedis()) {
            return "OK".equals(jedis.set(key, value));
        } catch (Exception e) {
            return false;
        }
    }

    public Long decr(final String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.decr(key);
        } catch (Exception e) {
            return null;
        }
    }


    public Long incr(final String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.incr(key);
        } catch (Exception e) {
            return null;
        }
    }

    public Long incrBy(final String key, final long i) {
        try (Jedis jedis = getJedis()) {
            return jedis.incrBy(key, i);
        } catch (Exception e) {
            return null;
        }
    }

    public String get(final String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    public Long llen(final String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.llen(key);
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> lrange(final String key, final long start, final long end) {
        try (Jedis jedis = getJedis()) {
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            return null;
        }
    }

    public String lindex(final String key, final long index) {
        try (Jedis jedis = getJedis()) {
            return jedis.lindex(key, index);
        } catch (Exception e) {
            return null;
        }
    }

    public void del(final String key) {
        try (Jedis jedis = getJedis()) {
            jedis.del(key);
        } catch (Exception e) {
        }
    }

    public void append(final String key, final String value) {
        try (Jedis jedis = getJedis()) {
            jedis.append(key, value);
        } catch (Exception e) {
        }
    }

    public void append(final byte[] key, final byte[] value) {
        try (Jedis jedis = getJedis()) {
            jedis.append(key, value);
        } catch (Exception e) {
        }
    }

    public String hget(final String key, final String field) {
        try (Jedis jedis = getJedis()) {
            return jedis.hget(key, field);
        } catch (Exception e) {
            return null;
        }
    }

    public Long hset(final String key, final String field, final String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.hset(key, field, value);
        } catch (Exception e) {
            return null;
        }
    }

    public void hdel(final String key, final String field) {
        try (Jedis jedis = getJedis()) {
            jedis.hdel(key, field);
        } catch (Exception e) {
        }
    }

    public Set<String> hkeys(final String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hkeys(key);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean hmset(final String key, final Map<String, String> hash) {
        try (Jedis jedis = getJedis()) {
            return "OK".equals(jedis.hmset(key, hash));
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> hmget(final String key, final String[] fields) {
        try (Jedis jedis = getJedis()) {
            return jedis.hmget(key, fields);
        } catch (Exception e) {
            return null;
        }
    }

    public byte[] hget(final byte[] key, final byte[] field) {
        try (Jedis jedis = getJedis()) {
            return jedis.hget(key, field);
        } catch (Exception e) {
            return null;
        }
    }

    public Long zadd(final String key, final double score, final String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zadd(key, score, member);
        } catch (Exception e) {
            return null;
        }
    }

    public Double zincrby(final String key, final double score, final String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zincrby(key, score, member);
        } catch (Exception e) {
            return null;
        }
    }

    public Double zscore(final String key, final String element) {
        try (Jedis jedis = getJedis()) {
            return jedis.zscore(key, element);
        } catch (Exception e) {
            return null;
        }
    }

    public Long zcard(final String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.zcard(key);
        } catch (Exception e) {
            return null;
        }
    }

    public Long scard(final String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.scard(key);
        } catch (Exception e) {
            return null;
        }
    }

    public Long zrank(final String key, final String element) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrank(key, element);
        } catch (Exception e) {
            return null;
        }
    }

    public Long expire(final String key, final int seconds) {
        try (Jedis jedis = getJedis()) {
            return jedis.expire(key, seconds);
        } catch (Exception e) {
            return null;
        }
    }


    public Boolean exists(final String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.exists(key);
        } catch (Exception e) {
            return null;
        }
    }

    public Long ttl(final String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.ttl(key);
        } catch (Exception e) {
            return null;
        }
    }

    public Set<String> zrevrange(final String key, final long start, final long end) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            return null;
        }
    }

    public Long sadd(final String key, final String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.sadd(key, member);
        } catch (Exception e) {
            return null;
        }
    }

    public Long srem(final String key, final String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.srem(key, member);
        } catch (Exception e) {
            return null;
        }
    }

    public Long zrem(final String key, final String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrem(key, member);
        } catch (Exception e) {
            return null;
        }
    }

    public Set<String> smembers(final String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.smembers(key);
        } catch (Exception e) {
            return null;
        }
    }

    public String spop(final String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.spop(key);
        } catch (Exception e) {
            return null;
        }
    }

    public String srandmember(final String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.srandmember(key);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean sismember(final String key, final String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.sismember(key, member);
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean setnxEx(final String key, final String value, final int seconds) {
        try (Jedis jedis = getJedis()) {
            Long nxValue = jedis.setnx(key, value);
            if (nxValue == 0) {
                return false;
            }
            String exValue = jedis.setex(key, seconds, value);
            if (exValue != null && exValue.equals("OK")) {
                return true;
            }
            exValue = jedis.setex(key, seconds, value);
            return exValue != null && exValue.equals("OK");
        } catch (Exception e) {
            return false;
        }
    }

    public Long hlen(final String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hlen(key);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public boolean setObject(String key, int seconds, Object object) {
        try (Jedis jedis = getJedis()) {
            if (seconds <= 0) {
                return "OK".equals(jedis.set(key.getBytes(), SerializeUtils.serialize(object)));
            }
            else {
                return "OK".equals(jedis.setex(key.getBytes(), seconds, SerializeUtils.serialize(object)));
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getSet(String key, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.getSet(key, value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Object getObject(String key) {
        try (Jedis jedis = getJedis()) {
            byte[] in = jedis.get(key.getBytes());
            return SerializeUtils.deserialize(in);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
