package com.msdn.generator.common.exception;

import com.msdn.generator.common.entity.ExceptionEnum;
import com.msdn.generator.common.entity.IError;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2021/5/3 20:59
 * @description 业务异常信息处理
 */
@Getter
@Setter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误描述
     */
    private String errorMsg;

    public BusinessException() {
        super();
    }

    public BusinessException(IError error) {
        super(error.getResultCode());
        this.errorCode = error.getResultCode();
        this.errorMsg = error.getResultMsg();
    }

    public BusinessException(IError error, Throwable cause) {
        super(error.getResultCode(), cause);
        this.errorCode = error.getResultCode();
        this.errorMsg = error.getResultMsg();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String errorCode, String errorMsg) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public static void validateFailed(String message) {
        throw new BusinessException(ExceptionEnum.VALIDATE_FAILED.getResultCode(), message);
    }

    public static void fail(String message) {
        throw new BusinessException(message);
    }

    public static void fail(IError error) {
        throw new BusinessException(error);
    }

    public static void fail(String errorCode, String errorMsg) {
        throw new BusinessException(errorCode, errorMsg);
    }
}
