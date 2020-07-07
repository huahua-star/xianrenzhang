package org.jeecg.modules.zzj.util.Card;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-07-17 21:12
 */
@Component
public class RedisUtils {
    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource(name="redisTemplate")
    private ValueOperations<String, String> valueOperations;
    @Resource(name="redisTemplate")
    private HashOperations<String, String, Object> hashOperations;
    @Resource(name="redisTemplate")
    private ListOperations<String, Object> listOperations;
    @Resource(name="redisTemplate")
    private SetOperations<String, Object> setOperations;
    @Resource(name="redisTemplate")
    private ZSetOperations<String, Object> zSetOperations;
    /**  默认过期时长，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1;

    @SuppressWarnings("unchecked")
    public void set(String key, Object value, long expire){
        valueOperations.set(key, toJson(value));
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void lpush(String key,Object value){
        listOperations.leftPush(key,value);
    }

    public void rpush(String key,Object value){
        listOperations.rightPush(key,value);
    }

    public <T> T rpop (String key,Class<T>  clazz){
        Object object = listOperations.rightPop(key);
        String json = toJson(object);
        return json == null ? null : fromJson(json,clazz);
    }

    public void set(String key, Object value){
        set(key, value, DEFAULT_EXPIRE);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : JSON.parseArray(value, clazz);
    }

  /*public Object getObjects(String key){
    	Object object = (Object)redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Jedis jedis = (Jedis) redisConnection.getNativeConnection();
                jedis.select(0);
                String obj = jedis.get(key);
                return obj;
            }
        });
		return object;

    }*/

   /* public <T> Object JSONToObject(String key,Session session) {
        Gson gson = new Gson();
        String jsonstring = (String)redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Jedis jedis = (Jedis) redisConnection.getNativeConnection();
                jedis.select(0);
                String str = jedis.get(key);
                return str;
            }
        });
        Object object = gson.fromJson(jsonstring, session.getClass());
		return object;
    }*/

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public <T> List<T>  getList(String key, Class<T> clazz) {
        return getList(key, clazz,NOT_EXPIRE);
    }

    @SuppressWarnings("unchecked")
    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    @SuppressWarnings("unchecked")
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object){
        if(object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String){
            return String.valueOf(object);
        }
        return JSON.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz){
        return JSON.parseObject(json, clazz);
    }
}
