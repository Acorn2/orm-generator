package com.msdn.generator.common.entity;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/ 枚举了一些常用API操作码
 */
public enum ExceptionEnum implements IError {
    // 数据操作状态码和提示信息定义
    SUCCESS("200", "操作成功"),
    VALIDATE_FAILED("400", "参数检验失败"),
    NOT_FOUND("404", "参数检验失败"),
    UNAUTHORIZED("401", "暂未登录或token已经过期"),
    FORBIDDEN("403", "没有相关权限"),
    REQUEST_TIME_OUT("408", "请求时间超时"),
    INTERNAL_SERVER_ERROR("500", "服务器内部错误!"),
    SERVER_BUSY("503", "服务器正忙，请稍后再试!");
    /**
     * 错误码
     */
    private String resultCode;

    /**
     * 错误描述
     */
    private String resultMsg;

    private ExceptionEnum(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }


    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }
}
