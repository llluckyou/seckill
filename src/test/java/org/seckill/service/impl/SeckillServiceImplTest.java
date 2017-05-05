package org.seckill.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/5/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
                       "classpath:spring/spring-service.xml"
})

public class SeckillServiceImplTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckills = seckillService.getSeckillList();
        //{}为占位符
        logger.info("list={}", seckills);

    }

    @Test
    public void getSeckillById() throws Exception {
        long id = 1004;
        Seckill seckill = seckillService.getSeckillById(id);
        logger.info("id 1004 + {}", seckill);
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long id = 1006;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("秒杀接口 + {}" ,exposer);
    }

    @Test
    public void executeSeckill() throws Exception {
        long id = 1006L;
        long userPhone = 13992116652L;
        Exposer exposer = seckillService.exportSeckillUrl(id);

        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(id, userPhone, exposer.getMd5());
            logger.info("秒杀结果 + {}", seckillExecution);
        }catch (RepeatKillException e) {
            logger.error(e.getMessage());
        }catch (SeckillCloseException e) {
            logger.error(e.getMessage());
        }

    }

    @Test
    public void seckillLogic() throws Exception {
        long id = 1006;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if(exposer.isExposer()) {
            try {
                long userPhone = 13992116652L;
                SeckillExecution seckillExecution = seckillService.executeSeckill(id, userPhone, exposer.getMd5());
                logger.info("秒杀结果 + {}", seckillExecution);
            }catch (RepeatKillException e) {
                logger.error(e.getMessage());
            }catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            }
        }else {
            logger.info("秒杀接口 + {}" ,exposer);
        }
    }

    @Test
    public void seckillProcedure() throws Exception {
        long id = 1006;
        long userPhone = 96385274115L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if(exposer.isExposer()) {
            String md5 = exposer.getMd5();
            SeckillExecution seckillExecution = seckillService.executeSeckillProcedure(id, userPhone, md5);
            logger.info(seckillExecution.getStateInfo());
        }
    }

}