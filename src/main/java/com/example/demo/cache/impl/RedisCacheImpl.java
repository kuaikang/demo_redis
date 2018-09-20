package com.example.demo.cache.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import com.example.demo.cache.RedisCache;
import com.example.demo.exception.BusinessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

/**
 * @author kuaik
 * @date 2018-09-20
 */
public class RedisCacheImpl implements RedisCache {

    public static final long ONE_SECOND = 1L;

    private RedisTemplate<String, Object> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            throw new BusinessException("redis设置超时失败", e);
        }
    }

    @Override
    public long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new BusinessException("redis查询失败", e);
        }
    }

    @Override
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            throw new BusinessException("redis查询失败", e);
        }
    }

    @Override
    public void del(String... key) {
        try {
            if (key != null && key.length > 0) {
                if (key.length == 1) {
                    redisTemplate.delete(key[0]);
                } else {
                    redisTemplate.delete(CollectionUtils.arrayToList(key));
                }
            }
        } catch (Exception e) {
            throw new BusinessException("redis删除失败", e);
        }
    }

    @Override
    public Object get(String key) {
        try {
            return key == null ? null : redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            throw new BusinessException("redis查询失败", e);
        }
    }

    @Override
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public void set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public long incr(String key, long delta) {

        try {
            if (delta < 0) {
                throw new BusinessException("递增因子必须大于0");
            }
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public long decr(String key, long delta) {
        try {
            if (delta < 0) {
                throw new BusinessException("递减因子必须大于0");
            }
            return redisTemplate.opsForValue().increment(key, -delta);
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public Object hget(String key, String item) {
        try {
            return redisTemplate.opsForHash().get(key, item);
        } catch (Exception e) {
            throw new BusinessException("redis查询失败", e);
        }
    }

    @Override
    public List<Object> hValues(String key) {
        try {
            return redisTemplate.opsForHash().values(key);
        } catch (Exception e) {
            throw new BusinessException("redis查询失败", e);
        }
    }

    @Override
    public Map<Object, Object> hmget(String key) {

        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            throw new BusinessException("redis查询失败", e);
        }
    }

    @Override
    public void hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public void hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public void hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public void hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public void hdel(String key, Object... item) {
        try {
            redisTemplate.opsForHash().delete(key, item);
        } catch (Exception e) {
            throw new BusinessException("redis删除失败", e);
        }
    }

    @Override
    public boolean hHasKey(String key, String item) {
        try {
            return redisTemplate.opsForHash().hasKey(key, item);
        } catch (Exception e) {
            throw new BusinessException("redis查询失败", e);
        }
    }

    @Override
    public double hincr(String key, String item, double by) {
        try {
            return redisTemplate.opsForHash().increment(key, item, by);
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public double hdecr(String key, String item, double by) {

        try {
            return redisTemplate.opsForHash().increment(key, item, -by);
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            throw new BusinessException("redis查询失败", e);
        }
    }

    @Override
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            throw new BusinessException("redis查询失败", e);
        }
    }

    @Override
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            throw new BusinessException("redis删除失败", e);
        }
    }

    @Override
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            throw new BusinessException("redis查询失败", e);
        }
    }

    @Override
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            throw new BusinessException("redis查询失败", e);
        }
    }

    @Override
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            throw new BusinessException("redis查询失败", e);
        }
    }

    @Override
    public void lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public void lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public void lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public void lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public void lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            throw new BusinessException("redis删除失败", e);
        }
    }


    @Override
    public Long rpush(String key, String value) {
        try {
            return redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public Long lpush(String key, String value) {

        try {
            return redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        }
    }

    @Override
    public String lpop(String key) {

        try {
            Object value = redisTemplate.opsForList().leftPop(key);
            if (value == null) {
                return null;
            }
            return String.valueOf(value);
        } catch (Exception e) {
            throw new BusinessException("redis查询失败", e);
        }
    }

    @Override
    public String rpop(String key) {
        try {
            Object value = redisTemplate.opsForList().rightPop(key);
            if (value == null) {
                return null;
            }
            return String.valueOf(value);
        } catch (Exception e) {
            throw new BusinessException("redis查询失败", e);
        }
    }

    @Override
    public Boolean setnx(String key, String value) {
        RedisConnection connection = null;
        RedisConnectionFactory connectionFactory = null;
        try {
            connectionFactory = redisTemplate.getConnectionFactory();
            connection = connectionFactory.getConnection();
            return connection.setNX(key.getBytes(), value.getBytes());

        } catch (Exception e) {
            throw new BusinessException("redis保存失败", e);
        } finally {
            RedisConnectionUtils.releaseConnection(connection, connectionFactory);
        }
    }

    @Override
    public Boolean exceedMaxCount(String pattern, long maxCount) {

        long count = 0L;
        if (hasKey(pattern)) {
            count = incr(pattern, 1L);
        } else {
            count = incr(pattern, 1L);
            expire(pattern, ONE_SECOND);
        }
        if (count > maxCount) {
            // 删除key. 由于没有使用redis事务,高并发时,保证该key不会永久存在
            del(pattern);
            return true;
        }
        return false;
    }
}
