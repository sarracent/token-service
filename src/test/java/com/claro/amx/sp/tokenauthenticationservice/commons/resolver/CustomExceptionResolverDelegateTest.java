package com.claro.amx.sp.tokenauthenticationservice.commons.resolver;


import com.claro.amx.sp.tokenauthenticationservice.exception.impl.BusinessException;
import com.claro.amx.sp.tokenauthenticationservice.model.response.ServiceResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;

import javax.validation.ConstraintViolationException;
import java.util.Set;

import static com.claro.amx.sp.tokenauthenticationservice.constants.Constants.ERROR_LEVEL;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomExceptionResolverDelegateTest {

    @Test
    void buildServiceResponse() {
       Exception ex = new Exception();
       ServiceResponse serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(ex);
       assertEquals("900000", serviceResponse.getServiceDetails().getCode());
    }

    @Test
    void buildServiceResponseCustomException() {
        BusinessException ex = new BusinessException("100101", "El Token: %s no es válido", ERROR_LEVEL);
        ServiceResponse serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(ex);
        assertEquals("100101", serviceResponse.getServiceDetails().getCode());
    }

    @Test
    void buildServiceResponseBadCredentialsException() {
        BadCredentialsException ex = new BadCredentialsException("El channel %s no está autorizado");
        ServiceResponse serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(ex);
        assertEquals("100104", serviceResponse.getServiceDetails().getCode());
    }

    @Test
    void buildServiceResponse2() {
        ConstraintViolationException cv = new ConstraintViolationException("No se puede procesar la informacion entrante", Set.of());
        ServiceResponse serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(cv);
        assertEquals("100400", serviceResponse.getServiceDetails().getCode());
    }

    @Test
    void buildServiceResponse3() {
        HttpMessageNotReadableException http = new HttpMessageNotReadableException("Error el campo ratingGroupList debería ser un array");
        ServiceResponse serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(http);
        assertEquals("900000", serviceResponse.getServiceDetails().getCode());
        assertEquals(http.getMessage(), "Error el campo ratingGroupList debería ser un array");
    }

    @Test
    void buildServiceResponse4() {
        RedisConnectionFailureException rfe = new RedisConnectionFailureException("");
        ServiceResponse serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(rfe);
        assertEquals("900000", serviceResponse.getServiceDetails().getCode());
    }
}