package org.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/3.
 */

@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    //盐值字符串，用于生成加密字符串md5
    private final String slat = "anSdfASDinNAOISghjdNODS5678%^&*)()$$^&)()>";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,100);
    }

    public Seckill getSeckillById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        //缓存优化redis
        /**
         * get from cache
         * if null
         *  get db
         *  else
         *      put cache
         *
         */
        //1.先访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if(seckill == null) {
            // 在访问数据库
            seckill = seckillDao.queryById(seckillId);
            if(seckill == null) {
                throw new SeckillException("未找到秒杀信息");
            }else {
                //把数据放入redis
                redisDao.putSeckill(seckill);
            }
        }

        Date startTime = seckill.getStartTime();
        Date nowTime = new Date();
        Date endTime = seckill.getEndTime();

        if(nowTime.getTime() < startTime.getTime() ||
                nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        String md5 = getMD5(seckillId);
        return new Exposer(true, seckillId, md5);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 读操作和一条修改操作不需要事务
     * 尽量控制事务的执行时间，并发量大的网站中将成为影响性能的很重要的点
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) {

        if(md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("md5验证失败");
        }

        try {
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if(insertCount == 1) {
                Date nowTime = new Date();

                //热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if(updateCount != 0) {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return  new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }else {
                    throw new SeckillCloseException("秒杀失败");
                }
            }else {
                throw new RepeatKillException("重复秒杀");
            }
        }catch (RepeatKillException e1) {
            throw e1;
        }catch (SeckillCloseException e2) {
            throw e2;
        }catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            //把非必检异常重新抛出
            throw new SeckillException(e.getMessage());
        }
    }

    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) throws SeckillException, SeckillCloseException, RepeatKillException {
        if(md5 == null || !md5.equals(getMD5(seckillId))) {
            return new SeckillExecution(seckillId, SeckillStatEnum.DATA_REWRITE);
        }

        Map<String, Object> map = new HashMap();
        Date killTime = new Date();

        map.put("seckillId", seckillId);
        map.put("userPhone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);

        try {
            seckillDao.killByProcedure(map);
            int result = MapUtils.getInteger(map, "result", -2);
            if(result != 1) {
                return new SeckillExecution(seckillId, SeckillStatEnum.statOf(result));
            }else {
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }
    }
}
