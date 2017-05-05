package org.seckill.dto;

/**
 * Created by Administrator on 2017/5/3.
 */
public class Exposer {

    //秒杀是否开启
    private boolean exposer;

    private long seckillId;
    private String md5;

    //当前时间
    private long now;

    private long start;

    private long end;

    public Exposer(boolean exposer, long seckillId, String md5) {
        this.exposer = exposer;
        this.seckillId = seckillId;
        this.md5 = md5;
    }

    public Exposer(boolean exposer, long now, long start, long end) {
        this.exposer = exposer;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(boolean exposer, long seckillId) {
        this.exposer = exposer;
        this.seckillId = seckillId;
    }

    public boolean isExposer() {
        return exposer;
    }

    public void setExposer(boolean exposer) {
        this.exposer = exposer;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "exposer=" + exposer +
                ", seckillId=" + seckillId +
                ", md5='" + md5 + '\'' +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
