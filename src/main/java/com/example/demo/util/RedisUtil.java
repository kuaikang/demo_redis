package com.example.demo.util;

import com.example.demo.cache.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author Administrator Redis 工具类
 */
public class RedisUtil {

  @Autowired
  private RedisCache redisCache;

  private static RedisUtil redisUtil;

  @PostConstruct
  private void init() {
    redisUtil = this;
    redisUtil.redisCache = this.redisCache;
  }

  /**
   * 获得注入的redisCache接口
   *
   * @return redisCache接口
   */
  public static RedisCache getRedisCache() {
    return redisUtil.redisCache;
  }

  /**
   * 设置 String
   *
   * @param key 格式要求："项目名:模块名:key"，例如："authcenter:account:token"
   */
  public static void set(String key, String value) {
    getRedisCache().set(key, value);
  }


  /**
   * 设置 String 过期时间
   *
   * @param seconds 以秒为单位
   */
  public static void set(String key, String value, int seconds) {
    getRedisCache().set(key, value, seconds);
  }


  /**
   * 设置 String
   *
   * @param key 格式要求："项目名:模块名:key"，例如："authcenter:account:token"
   */
  public static void set(String key, Object value) {

    getRedisCache().set(key, value);
  }

  /**
   * 设置 String 过期时间
   *
   * @param seconds 以秒为单位
   */
  public static void set(String key, Object value, int seconds) {

    getRedisCache().set(key, value, seconds);
  }

  /**
   * 获取String值
   *
   * @return value
   */
  public static <T> T get(String key, Class<T> clazz) {

    Object value = getRedisCache().get(key);
    if (value == null) {
      return null;
    }

    return (T) value;
  }

  /**
   * 获取String值
   *
   * @return value
   */
  public static String get(String key) {

    Object value = getRedisCache().get(key);
    if (null == value) {
      return null;
    }
    return String.valueOf(value);
  }


  /**
   * 删除值
   */
  public static void remove(String key) {
    getRedisCache().del(key);
  }


  /**
   * lpush
   */
  public static void lpush(String key, String value) {
    getRedisCache().lpush(key, value);
  }

  /**
   * rpush
   */
  public static void rpush(String key, String value) {
    getRedisCache().rpush(key, value);
  }

  /**
   * lrem
   */
  public static void lrem(String key, long count, String value) {
    getRedisCache().lRemove(key, count, value);
  }

  /**
   * rpop
   */
  public static String rpop(String key) {
    return getRedisCache().rpop(key);
  }

  /**
   * 锁住key
   *
   * @param key key
   * @param value value
   * @param seconds seconds
   * @return true:成功锁住key，false:key被其他服务占用而无法锁住
   */
  public synchronized static boolean lockKey(String key, String value, int seconds) {
    boolean lockSuccess = true;

    // 如果已经存在key
    if (!getRedisCache().setnx(key, value)) {
      lockSuccess = false;
    }
    getRedisCache().expire(key, seconds);
    return lockSuccess;
  }

  /**
   * hashset
   * @param h
   * @param hk
   * @param hv
   */
  public static void hset(String h, String hk, Object hv){
    getRedisCache().hset(h, hk, hv);
  }

  /**
   * hashget
   * @param h
   * @param hk
   * @return
   */
  public static Object hget(String h, String hk){
    return getRedisCache().hget(h, hk);
  }

}
