package com.msdn.generator.common.aop;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.msdn.generator.common.entity.RequestLog;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 * AOP输出请求日志
 *
 * @author hresh
 * @博客 https://juejin.cn/user/2664871918047063
 * @网站 https://www.hreshhao.com/
 * @date 2021/4/1
 */
@Component
@Aspect
@Order(1)
@Slf4j
public class RequestLogAspect {

    @Pointcut("execution(* com.msdn.orm.hresh.controller..*(..))")
    public void requestServer() {
        // TODO document why this method is empty
    }

    @Around("requestServer()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        //获取当前请求对象
        RequestLog requestLog = getRequestLog();

        Object result = proceedingJoinPoint.proceed();
        Signature signature = proceedingJoinPoint.getSignature();
        // 请求方法名（绝对路径）
        requestLog.setClassMethod(String.format("%s.%s", signature.getDeclaringTypeName(),
                signature.getName()));
        // 请求参数
        requestLog.setRequestParams(getRequestParamsByProceedingJoinPoint(proceedingJoinPoint));
        // 返回结果
        requestLog.setResult(result);
        // 如果返回结果不为null，则从返回结果中剔除返回数据，查看条目数、返回状态和返回信息等
        if (!ObjectUtils.isEmpty(result)) {
            JSONObject jsonObject = JSONUtil.parseObj(result);
            Object data = jsonObject.get("data");
            if (!ObjectUtils.isEmpty(data) && data.toString().length() > 200) {
                // 减少日志记录量，比如大量查询结果，没必要记录
                jsonObject.remove("data");
                requestLog.setResult(jsonObject);
            }
        }

        // 获取请求方法的描述注解信息
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(Operation.class)) {
            Operation methodAnnotation = method.getAnnotation(Operation.class);
            requestLog.setMethodDesc(methodAnnotation.description());
        }
        // 消耗时间
        requestLog.setTimeCost(System.currentTimeMillis() - start);

        log.info("Request Info      : {}", JSONUtil.toJsonStr(requestLog));
//    log.info(Markers.appendEntries(this.standELK(requestLog)), JSONUtil.toJsonStr(requestLog));
        return result;
    }

    @AfterThrowing(pointcut = "requestServer()", throwing = "e")
    public void doAfterThrow(JoinPoint joinPoint, RuntimeException e) {
        try {
            RequestLog requestLog = getRequestLog();

            Signature signature = joinPoint.getSignature();
            // 请求方法名（绝对路径）
            requestLog.setClassMethod(String.format("%s.%s", signature.getDeclaringTypeName(),
                    signature.getName()));
            // 请求参数
            requestLog.setRequestParams(getRequestParamsByJoinPoint(joinPoint));
            StackTraceElement[] stackTrace = e.getStackTrace();
            // 将异常信息转换成json
            JSONObject jsonObject = new JSONObject();
            if (!ObjectUtils.isEmpty(stackTrace)) {
                StackTraceElement stackTraceElement = stackTrace[0];
                jsonObject = JSONUtil.parseObj(JSONUtil.toJsonStr(stackTraceElement));
                // 转换成json
                jsonObject.set("errorContent", e.getMessage());
                jsonObject.set("createTime", DateUtil.date());
                jsonObject.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
                jsonObject.set("messageId", IdUtil.fastSimpleUUID());
                // 获取IP地址
                jsonObject.set("serverIp", NetUtil.getLocalhostStr());
            }
            requestLog.setErrorMessage(jsonObject);
            log.error("Error Request Info      : {}", JSONUtil.toJsonStr(requestLog));
//            log.info(Markers.appendEntries(jsonObject), JSONUtil.toJsonStr(requestLog));
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    private RequestLog getRequestLog() {
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        // 记录请求信息(通过Logstash传入Elasticsearch)
        RequestLog requestLog = new RequestLog();
        if (!ObjectUtils.isEmpty(attributes) && !ObjectUtils.isEmpty(attributes.getRequest())) {
            HttpServletRequest request = attributes.getRequest();
            // 请求ip
            requestLog.setIp(request.getRemoteAddr());
            // 访问url
            requestLog.setUrl(request.getRequestURL().toString());
            // 请求类型
            requestLog.setHttpMethod(request.getMethod());
        }
        return requestLog;
    }

    /**
     * 根据方法和传入的参数获取请求参数
     *
     * @param proceedingJoinPoint 入参
     * @return 返回
     */
    private Map<String, Object> getRequestParamsByProceedingJoinPoint(
            ProceedingJoinPoint proceedingJoinPoint) {
        //参数名
        String[] paramNames = ((MethodSignature) proceedingJoinPoint.getSignature())
                .getParameterNames();
        //参数值
        Object[] paramValues = proceedingJoinPoint.getArgs();

        return buildRequestParam(paramNames, paramValues);
    }

    private Map<String, Object> getRequestParamsByJoinPoint(JoinPoint joinPoint) {
        try {
            //参数名
            String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
            //参数值
            Object[] paramValues = joinPoint.getArgs();

            return buildRequestParam(paramNames, paramValues);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private Map<String, Object> buildRequestParam(String[] paramNames, Object[] paramValues) {
        try {
            Map<String, Object> requestParams = new HashMap<>(paramNames.length);
            for (int i = 0; i < paramNames.length; i++) {
                Object value = paramValues[i];

                //如果是文件对象
                if (value instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) value;
                    //获取文件名
                    value = file.getOriginalFilename();
                }

                requestParams.put(paramNames[i], value);
            }

            return requestParams;
        } catch (Exception e) {
            return new HashMap<>(1);
        }
    }

    /**
     * 挑选关键信息输出到ELK
     *
     * @param requestLog 日志请求记录对象
     * @return 返回一个map对象
     */
    private Map<String, Object> standELK(RequestLog requestLog) {
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("url", requestLog.getUrl());
        logMap.put("method", requestLog.getClassMethod());
        logMap.put("requestParams", requestLog.getRequestParams());
        logMap.put("timeCost", requestLog.getTimeCost());
        logMap.put("methodDesc", requestLog.getMethodDesc());
        return logMap;
    }

}
