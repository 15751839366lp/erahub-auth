package com.erahub.common.response;

import lombok.Data;

@Data
public class ResponseBean<T> {

    /** 200:操作成功  -1：操作失败**/

    // http 状态码
    private boolean success;

    //200正常，非200异常
    private int code;

    private String msg;

    // 返回的数据
    private T data;

    public static <T> ResponseBean<T> success(T data) {
        ResponseBean<T> responseBean = new ResponseBean<>();
        responseBean.setSuccess(true);
        responseBean.setData(data);
        return responseBean;
    }


    public static <T> ResponseBean<T> error(T errorData) {
        ResponseBean<T> responseBean = new ResponseBean<>();
        responseBean.setSuccess(false);
        responseBean.setData(errorData);
        return responseBean;
    }

    public static <T> ResponseBean<T> success() {
        ResponseBean<T> responseBean = new ResponseBean<>();
        responseBean.setSuccess(true);
        return responseBean;
    }

    public static <T> ResponseBean<T> succ(T data) {
        ResponseBean<T> r = new ResponseBean<T>();
        r.setCode(200);
        r.setMsg("操作成功");
        r.setData(data);
        return r;
    }

    public static  <T> ResponseBean<T> succ(String msg, T data) {
        ResponseBean<T> r = new ResponseBean<T>();
        r.setCode(200);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }

    public static  <T> ResponseBean<T> succ(int code, String msg, T data) {
        ResponseBean<T> r = new ResponseBean<T>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }

    public static  <T> ResponseBean<T> fail(String msg) {
        ResponseBean<T> r = new ResponseBean<T>();
        r.setCode(400);
        r.setData(null);
        r.setMsg(msg);
        return r;
    }

    public static  <T> ResponseBean<T> fail(String msg, T data) {
        ResponseBean<T> r = new ResponseBean<T>();
        r.setCode(400);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }

    public static  <T> ResponseBean<T> fail(int code, String msg, T data) {
        ResponseBean<T> r = new ResponseBean<T>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }
}
