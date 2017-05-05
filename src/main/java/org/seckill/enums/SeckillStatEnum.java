package org.seckill.enums;

/**
 * Created by Administrator on 2017/5/3.
 */
public enum SeckillStatEnum {
    SUCCESS(1, "秒杀成功"),
    END(0, "秒杀结束"),
    REPEAT_KILL(-1, "重复秒杀"),
    INNER_ERROR(-2, "系统错误"),
    DATA_REWRITE(-3, "数据篡改")
    ;
    private int stat;
    private String statInfo;

    SeckillStatEnum(int stat, String statInfo) {
        this.stat = stat;
        this.statInfo = statInfo;
    }

    public int getStat() {
        return stat;
    }

    public String getStatInfo() {
        return statInfo;
    }

    public static SeckillStatEnum statOf(int stat) {
        for(SeckillStatEnum statEnum : values()) {
            if(statEnum.getStat() == stat) {
                return statEnum;
            }
        }
        return null;
    }
}
