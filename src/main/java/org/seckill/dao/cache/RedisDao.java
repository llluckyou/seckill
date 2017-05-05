package org.seckill.dao.cache;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Administrator on 2017/5/4.
 */
public class RedisDao {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private JedisPool jedisPool;

    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public Seckill getSeckill(long seckillId) {
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckillId;
                //并没有序列化
                //protostuff : pojo
                byte[] bytes = jedis.get(key.getBytes());
                if(bytes != null) {
                    //空对象
                    Seckill seckill = schema.newMessage();
                    //将序列传递给空对象
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);

                    return seckill;
                }
            }finally {
                jedis.close();
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    //缓存时间(一个小时)
    private final int time = 3600;

    public String putSeckill(Seckill seckill) {
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                String result = jedis.setex(key.getBytes(), time, bytes);
                return result;
            }finally {
                jedis.close();
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
