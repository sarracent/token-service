package com.claro.amx.sp.tokenauthenticationservice.commons.handler;


import com.claro.amx.sp.tokenauthenticationservice.commons.aop.LogAspect;
import com.claro.amx.sp.tokenauthenticationservice.commons.resolver.CustomExceptionResolverDelegate;
import com.claro.amx.sp.tokenauthenticationservice.exception.impl.*;
import com.claro.amx.sp.tokenauthenticationservice.model.response.ServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({TechnicalException.class, DataBaseException.class, BadCredentialsException.class, Exception.class})
    public ResponseEntity<ServiceResponse> handleOtherExceptions(Exception exception, WebRequest request) {
        final var serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(exception);
        LogAspect.logFinishOperationInError(serviceResponse);
        return ResponseEntity.ok().body(serviceResponse);
    }

    @ExceptionHandler({InternalException.class})
    public ResponseEntity<ServiceResponse> handleInternalExceptions(Exception exception, WebRequest request) {
        final var serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(exception);
        LogAspect.logFinishOperationInError(serviceResponse);
        return ResponseEntity.internalServerError().body(serviceResponse);
    }

    @ExceptionHandler({ControllersException.class, ConstraintViolationException.class})
    public ResponseEntity<ServiceResponse> handleControllersExceptions(Exception exception, WebRequest request) {
        final var serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(exception);
        LogAspect.logFinishOperationInError(serviceResponse);
        return ResponseEntity.badRequest().body(serviceResponse);
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ServiceResponse> handleBusinessExceptions(Exception exception, WebRequest request) {
        final var serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(exception);
        LogAspect.logFinishOperationInError(serviceResponse);
        return ResponseEntity.ok(serviceResponse);
    }

}