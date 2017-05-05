package org.seckill.dto;

/**
 * 用于封装json，返回结果
 * Created by Administrator on 2017/5/3.
 */

public class SeckillResult<T> {
    private boolean success;
    private T data;
    private String error;

    //返回正确数据
    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    //返回错误信息
    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
