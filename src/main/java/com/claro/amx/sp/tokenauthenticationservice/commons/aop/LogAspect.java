package com.claro.amx.sp.tokenauthenticationservice.commons.aop;


import com.claro.amx.sp.tokenauthenticationservice.annotations.auditable.AuditableApi;
import com.claro.amx.sp.tokenauthenticationservice.annotations.log.LogException;
import com.claro.amx.sp.tokenauthenticationservice.commons.resolver.CustomHeadersResolver;
import com.claro.amx.sp.tokenauthenticationservice.constants.Constants;
import com.claro.amx.sp.tokenauthenticationservice.constants.Errors;
import com.claro.amx.sp.tokenauthenticationservice.exception.CustomException;
import com.claro.amx.sp.tokenauthenticationservice.model.response.ServiceResponse;
import com.claro.amx.sp.tokenauthenticationservice.utility.LogUtil;
import com.claro.amx.sp.tokenauthenticationservice.utility.Logs;
import com.claro.amx.sp.tokenauthenticationservice.utility.Util;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.claro.amx.sp.tokenauthenticationservice.constants.Constants.*;
import static com.claro.amx.sp.tokenauthenticationservice.utility.Logs.Basic.*;
import static com.claro.amx.sp.tokenauthenticationservice.utility.Logs.Extras.ERROR_EXECUTE;
import static com.claro.amx.sp.tokenauthenticationservice.utility.Logs.Extras.START_TIME;
import static com.claro.amx.sp.tokenauthenticationservice.utility.Logs.Header.*;


@Aspect
@Component
public class LogAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LogAspect.class);

    LogAspect() {
        LogUtil.initializeStructure(EnumSet.allOf(Logs.Header.class), EnumSet.allOf(Logs.Basic.class));
    }

    public static String getExecutionTime() {
        final long start = ThreadContext.get(START_TIME.name()) == null ? 0 : Long.parseLong(ThreadContext.get(START_TIME.name()));
        return String.valueOf(System.currentTimeMillis() - start);
    }

    public static void logFinishOperationInError(Object response) {
        if (response instanceof ServiceResponse) {
            LogUtil.logFinishOperation(LogAspect.LOG, ((ServiceResponse) response).getServiceDetails().getMessage(), ((ServiceResponse) response).getServiceDetails().getCode(), getExecutionTime());
            LogUtil.logCleanAll();
        }
    }

    @Around("@annotation(com.claro.amx.sp.tokenauthenticationservice.annotations.auditable.AuditableApi)")
    public Object logExecutionTime(ProceedingJoinPoint jp) throws Throwable {
        ThreadContext.put(START_TIME.name(), String.valueOf((System.currentTimeMillis())));
        ThreadContext.put(ERROR_EXECUTE.name(), N);

        AuditableApi auditableApi = ((MethodSignature) jp.getSignature()).getMethod().getAnnotation(AuditableApi.class);
        String nameHttpHeadersMap = auditableApi.parameterIgnore().nameToAudit();
        Map<String, Object> parametersMap = getMethodParameters(jp);
        Map<String, String> headersMap = CustomHeadersResolver.getHeadersMap((Map<String, Object>) parametersMap.get(nameHttpHeadersMap));
        parametersMap.remove(nameHttpHeadersMap);

        updateLogParameter(getOperation(jp), jp.getSignature().getName(), parametersMap, headersMap);

        Object response = jp.proceed();

        ThreadContext.put(RESPONSE.name(), getResponse(response));

        LogUtil.logFinishOperation(LOG, Constants.ZERO_MSG, Constants.OK_MSG, getExecutionTime());
        LogUtil.logCleanAll();

        return response;
    }

    @Around("@annotation(com.claro.amx.sp.tokenauthenticationservice.annotations.log.LogDAO) || @annotation(com.claro.amx.sp.tokenauthenticationservice.annotations.log.LogBO) || @annotation(com.claro.amx.sp.tokenauthenticationservice.annotations.log.LogService)")
    public Object logOperationTime(ProceedingJoinPoint jp) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = jp.proceed();
        long executionTime = System.currentTimeMillis() - start;
        LogUtil.logIntermediateOperation(LOG, getOperation(jp), Constants.OK_MSG, Constants.SUCCESS_MSG, String.valueOf(executionTime), getRequest(jp), Util.toJSONString(proceed));
        return proceed;
    }

    @AfterReturning(value = "@annotation(org.springframework.web.bind.annotation.ExceptionHandler)", returning = "response")
    public void logFinishOperationInError(JoinPoint jp, Object response) {
        ServiceResponse serviceResponse = null;
        if (response instanceof ResponseEntity) {
            serviceResponse = (ServiceResponse)((ResponseEntity<?>)response).getBody();
        }

        if (response instanceof ServiceResponse) {
            serviceResponse = (ServiceResponse)response;
        }

        if (serviceResponse != null) {
            LogUtil.logFinishOperation(LOG, serviceResponse.getServiceDetails().getMessage(), serviceResponse.getServiceDetails().getCode(), getExecutionTime());
        }

        LogUtil.cleanThreadContext();
    }

    @AfterThrowing(pointcut = "logForAll()", throwing = "ex")
    public void afterAnyException(JoinPoint jp, Exception ex) {
        String errorExecute = Optional.ofNullable(ThreadContext.get(ERROR_EXECUTE.name())).orElse(N);
        Class<?> exceptionClass = ex.getClass();
        if (!(exceptionClass.isAnnotationPresent(LogException.class))) {
            if (errorExecute.equalsIgnoreCase(Y)) return;
            else ThreadContext.put(ERROR_EXECUTE.name(), Y);
        }
        String opName = getOperation(jp);
        String code;
        String message;
        if (ex instanceof CustomException) {
            CustomException customException = ((CustomException) ex);
            code = String.valueOf(customException.getCode());
            message = customException.getCode();
        } else {
            code = Errors.ERROR_GENERAL.getCode();
            message = String.format(Errors.ERROR_GENERAL.getMessage(), ex.getMessage());
        }
        String request = getRequest(jp);
        LogUtil.logErrorOperation(LOG, opName, code, message, getExecutionTime(), request, getResponseWithError(message, ex));
    }

    @Pointcut("within(com.claro.amx.sp.tokenauthenticationservice..*)")
    private void logForAll() {
        // Escucha en todos los metodos que pasen por el negocio
    }

    private Map<String, Object> getMethodParameters(ProceedingJoinPoint joinPoint) {
        try {
            Parameter[] parameters = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameters();
            if (parameters == null || parameters.length == 0) return new HashMap<>();
            return IntStream.range(0, parameters.length)
                    .boxed()
                    .map(index -> Tuple.of(parameters[index].getName(),
                            (joinPoint.getArgs()[index] != null ? joinPoint.getArgs()[index] : "")
                    )).collect(Collectors.toMap(Tuple2::_1, Tuple2::_2));
        } catch (Exception ex) {
            return new HashMap<>();
        }
    }

    private String getOperation(Object operation) {
        try {
            if (operation instanceof ProceedingJoinPoint) {
                ProceedingJoinPoint joinPoint = (ProceedingJoinPoint) operation;
                return String.format(Constants.OPNAME,
                        joinPoint.getSignature().getDeclaringType().getSimpleName(),
                        joinPoint.getSignature().getName());
            }
            return operation.toString();
        } catch (Exception ex) {
            return null;
        }
    }

    private String getRequest(Object request) {
        try {
            if (request instanceof ProceedingJoinPoint) {
                ProceedingJoinPoint joinPoint = (ProceedingJoinPoint) request;
                AuditableApi auditableApi = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(AuditableApi.class);
                Map<String, Object> parametersMap = getMethodParameters(joinPoint);
                if (auditableApi != null) parametersMap.remove(auditableApi.parameterIgnore().nameToAudit());
                return Util.toJSONString(parametersMap);
            }
            return Util.toJSONString(request);
        } catch (Exception ex) {
            return null;
        }
    }

    String getResponse(Object response) {
        try {
            if (response instanceof ResponseEntity)
                return Util.toJSONString(((ResponseEntity<?>) response).getBody());
            if (response instanceof Throwable)
                return Arrays.stream(((Throwable) response).getStackTrace()).findFirst().map(StackTraceElement::toString).orElse(null);
            return Util.toJSONString(response);
        } catch (Exception ex) {
            return null;
        }
    }

    private String getResponseWithError(String message, Throwable throwable) {
        try {
            return Util.getMsgWithLineCodeError(message, throwable);
        } catch (Exception ex) {
            return null;
        }
    }

    void updateLogParameter(String operationName, String methodName, Map<String, Object> parametersMap, Map<String, String> headersMap) {
        final String transactionId = Util.generateUniqueId(methodName);
        final String sessionId = headersMap.get(SESSION_NAME);
        final String channelId = headersMap.get(CHANNEL_NAME);

        ThreadContext.put(TRANSACTION_ID.name(), transactionId);
        ThreadContext.put(SESSION_ID.name(), sessionId);
        ThreadContext.put(CHANNEL_ID.name(), channelId);
        ThreadContext.put(OPERATION.name(), operationName);
        ThreadContext.put(REQUEST.name(), getRequest(parametersMap));
    }

}
