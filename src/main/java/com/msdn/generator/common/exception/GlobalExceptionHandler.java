package com.msdn.generator.common.exception;

import com.msdn.generator.common.entity.Result;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * 全局异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * 处理自定义的api异常
   *
   * @param e
   * @return
   */
  @ResponseBody
  @ExceptionHandler(value = BusinessException.class)
  public Result<Object> handle(BusinessException e) {
    if (Objects.nonNull(e.getErrorCode())) {
      log.error("发生业务异常！原因是：{}", e.getErrorMsg());
      return Result.failed(e.getErrorCode(), e.getErrorMsg());
    }
    return Result.failed(e.getMessage());
  }

  /**
   * 处理参数验证失败异常 基于json格式的数据传递，这种传递才会抛出MethodArgumentNotValidException异常
   *
   * @param e
   * @return
   */
  @ResponseBody
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public Result<Object> handleValidException(MethodArgumentNotValidException e) {
    BindingResult bindingResult = e.getBindingResult();
    String message = null;
    if (bindingResult.hasErrors()) {
      FieldError fieldError = bindingResult.getFieldError();
      if (Objects.nonNull(fieldError)) {
        message = fieldError.getField() + fieldError.getDefaultMessage();
      }
    }
    return Result.validateFailed(message);
  }

  /**
   * 使用@Validated 来校验 JavaBean的参数，比如@NotNull、@NotBlank等等; post 请求数据传递有两种方式，一种是基于form-data格式的数据传递，这种传递才会抛出BindException异常
   *
   * @param e
   * @return
   */
  @ResponseBody
  @ExceptionHandler(value = BindException.class)
  public Result<Object> handleValidException(BindException e) {
    BindingResult bindingResult = e.getBindingResult();
    String message = null;
    if (bindingResult.hasErrors()) {
      FieldError fieldError = bindingResult.getFieldError();
      if (fieldError != null) {
        message = fieldError.getField() + fieldError.getDefaultMessage();
      }
    }
    return Result.validateFailed(message);
  }
}
