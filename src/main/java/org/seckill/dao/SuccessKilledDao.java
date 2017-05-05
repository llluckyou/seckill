package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/5/2.
 */
@Repository
public interface SuccessKilledDao {

    /**
     * 插入秒杀记录
     * @param seckillId
     * @param userPhone
     * @return 返回插入行数
     */
    int insertSuccessKilled(long seckillId, long userPhone);

    /**
     *
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
