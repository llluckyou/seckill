package org.seckill.dao;

import org.seckill.entity.Seckill;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/2.
 */
@Repository
public interface SeckillDao {

    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return
     */
    int reduceNumber(long seckillId, Date killTime);

    /**
     * 根据主键查询秒杀
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 查询秒杀列表
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(int offset, int limit);

    /**
     * 使用存储过程执行秒杀
     */

    void killByProcedure(Map<String, Object> map);
}
