package com.claro.amx.sp.tokenauthenticationservice.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.claro.amx.sp.tokenauthenticationservice.exception.ExceptionType.CUSTOM_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CustomExceptionTest {
    private CustomException customException;

    @BeforeEach
    void setUp() {
        customException = new CustomException() {
            @Override
            public String getMessage() {
                return "0000";
            }

            @Override
            public String getCode() {
                return "Custom Exception Test";
            }

            @Override
            public String getLevel() {
                return "Error";
            }
        };
    }

    @Test
    void getExceptionType() {
        final ExceptionType exceptionType = customException.getExceptionType();
        assertEquals(CUSTOM_EXCEPTION, exceptionType);
    }

    @Test
    void getExtraInfo() {
        final List<Object> list = customException.getExtraInfo();
        assertNotNull(list);
    }
}