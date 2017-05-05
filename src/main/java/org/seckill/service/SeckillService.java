package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */
public interface SeckillService {

    /**
     * 获取所有秒杀
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 通过id获取对应的秒杀情况
     * @param seckillId
     * @return
     */
    Seckill getSeckillById(long seckillId);

    /**
     * 秒杀开启之前，显示当前时间和秒杀开启时间
     * 秒杀开启之后，暴露对应的秒杀接口
     * @param seckillId
     * @return Exposer
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return SeckillExecution
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, SeckillCloseException, RepeatKillException;

    /**
     * 通过调用存储过程执行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws SeckillCloseException
     * @throws RepeatKillException
     */

    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) throws SeckillException, SeckillCloseException, RepeatKillException;


}
