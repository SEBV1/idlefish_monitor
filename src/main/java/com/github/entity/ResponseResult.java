package com.github.entity;


public class ResponseResult<T> {
    private int code;
    private T data;
    private int count;
    private String msg;

    public ResponseResult(int code, T data, int count) {
        this.code = code;
        this.data = data;
        this.count = count;
    }

    public ResponseResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    // 成功响应，默认code为200
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(0, data);
    }

    public static <T> ResponseResult<T> success(T data, int count) {
        return new ResponseResult<>(0, data, count);
    }

    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(0, "成功");
    }

    // 失败响应，可自定义code
    public static <T> ResponseResult<T> failure(int code) {
        return new ResponseResult<>(code, null);
    }

    // 失败响应，可自定义code
    public static <T> ResponseResult<T> failure() {
        return new ResponseResult<>(500, null);
    }

    public static <T> ResponseResult<T> failure(String message) {
        return new ResponseResult<>(500, message);
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public int getCount() {
        return count;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}