package org.seckill.exception;

import org.seckill.dto.SeckillExecution;
import org.seckill.entity.SuccessKilled;

/**
 * 重复秒杀抛出的异常
 * Created by Administrator on 2017/5/3.
 */
public class RepeatKillException extends SeckillException {
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
