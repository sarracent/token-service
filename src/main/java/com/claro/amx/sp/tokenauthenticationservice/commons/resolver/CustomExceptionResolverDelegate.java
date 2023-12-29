package com.claro.amx.sp.tokenauthenticationservice.commons.resolver;

import com.claro.amx.sp.tokenauthenticationservice.exception.CustomException;
import com.claro.amx.sp.tokenauthenticationservice.model.response.ServiceDetails;
import com.claro.amx.sp.tokenauthenticationservice.model.response.ServiceResponse;
import org.springframework.security.authentication.BadCredentialsException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static com.claro.amx.sp.tokenauthenticationservice.constants.Constants.CHANNEL_NAME;
import static com.claro.amx.sp.tokenauthenticationservice.constants.Constants.SERVICE;
import static com.claro.amx.sp.tokenauthenticationservice.constants.Errors.*;

public class CustomExceptionResolverDelegate {

    private CustomExceptionResolverDelegate() {
    }

    public static ServiceResponse buildServiceResponse(Exception ex) {
        if (ex instanceof CustomException) {
            var iCustomException = (CustomException) ex;
            return ServiceResponse.builder()
                    .serviceDetails(
                            ServiceDetails.builder()
                                    .message(iCustomException.getMessage())
                                    .code(iCustomException.getCode())
                                    .level(iCustomException.getLevel())
                                    .service(SERVICE)
                                    .build()
                    )
                    .build();
        } else if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex;

            List<String> errors = new ArrayList<>();

            constraintViolationException.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));

            return ServiceResponse.builder()
                    .serviceDetails(
                            ServiceDetails.builder()
                                    .message(String.format(ERROR_BADREQUEST_GENERAL.getMessage(), constraintViolationException.getMessage()))
                                    .code(ERROR_BADREQUEST_GENERAL.getCode())
                                    .level(ERROR_BADREQUEST_GENERAL.getLevel())
                                    .service(SERVICE)
                                    .build()
                    )
                    .build();

        } else if (ex instanceof BadCredentialsException) {
            return ServiceResponse.builder()
                    .serviceDetails(ServiceDetails.builder()
                            .message(String.format(ERROR_CHANNEL_NOT_AUTHORIZED.getMessage(), CHANNEL_NAME))
                            .code(ERROR_CHANNEL_NOT_AUTHORIZED.getCode())
                            .level(ERROR_CHANNEL_NOT_AUTHORIZED.getLevel())
                            .service(SERVICE)
                            .build())
                    .build();

        } else {
            return ServiceResponse.builder()
                    .serviceDetails(
                            ServiceDetails.builder()
                                    .message(String.format(ERROR_GENERAL.getMessage(), ex.getMessage()))
                                    .code(ERROR_GENERAL.getCode())
                                    .level(ERROR_GENERAL.getLevel())
                                    .service(SERVICE)
                                    .build()
                    )
                    .build();
        }
    }

}