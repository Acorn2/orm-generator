package com.msdn.generator.common.entity;

import static com.alibaba.fastjson.JSON.toJSONString;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2021/4/16 9:27
 * @description 通用返回对象
 */
@Data
@NoArgsConstructor
public class Result<T> implements Serializable {

    private T data;
    private String code;
    private String message;
    private boolean success;

    protected Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = true;
    }

    protected Result(String code, String message, T data, boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    public static <T> Result<T> ok() {
        return ok((T) null);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @return
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(ExceptionEnum.SUCCESS.getResultCode(),
                ExceptionEnum.SUCCESS.getResultMsg(), data);
    }

    /**
     * 成功返回list结果
     *
     * @param list 获取的数据
     * @return
     */
    public static <T> Result<List<T>> ok(List<T> list) {
        return new Result<>(ExceptionEnum.SUCCESS.getResultCode(),
                ExceptionEnum.SUCCESS.getResultMsg(), list);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> Result<T> ok(T data, String message) {
        return new Result<>(ExceptionEnum.SUCCESS.getResultCode(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @param error 错误码
     */
    public static <T> Result<T> failed(IError error) {
        return new Result<>(error.getResultCode(), error.getResultMsg(), null, false);
    }

    /**
     * 失败返回结果
     *
     * @param error   错误码
     * @param message 错误信息
     */
    public static <T> Result<T> failed(IError error, String message) {
        return new Result<>(error.getResultCode(), message, null, false);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static <T> Result<T> failed(String errorCode, String message) {
        return new Result<>(errorCode, message, null, false);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> Result<T> failed(String message) {
        return new Result<>(ExceptionEnum.INTERNAL_SERVER_ERROR.getResultCode(), message, null,
                false);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> failed() {
        return failed(ExceptionEnum.INTERNAL_SERVER_ERROR);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> Result<T> validateFailed() {
        return failed(ExceptionEnum.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> Result<T> validateFailed(String message) {
        return new Result<>(ExceptionEnum.VALIDATE_FAILED.getResultCode(), message, null, false);
    }

    /**
     * 未登录返回结果
     */
    public static <T> Result<T> unauthorized(T data) {
        return new Result<>(ExceptionEnum.UNAUTHORIZED.getResultCode(),
                ExceptionEnum.UNAUTHORIZED.getResultMsg(), data, false);
    }

    /**
     * 未授权返回结果
     */
    public static <T> Result<T> forbidden(T data) {
        return new Result<>(ExceptionEnum.FORBIDDEN.getResultCode(),
                ExceptionEnum.FORBIDDEN.getResultMsg(), data, false);
    }

    @Override
    public String toString() {
        return toJSONString(this);
    }
}
