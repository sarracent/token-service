package com.claro.amx.sp.tokenauthenticationservice.exception;

import com.claro.amx.sp.tokenauthenticationservice.exception.impl.InternalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.claro.amx.sp.tokenauthenticationservice.exception.ExceptionType.CUSTOM_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class InternalExceptionTest {
    private InternalException internalException;

    @BeforeEach
    void setUp() {
        internalException = new InternalException("Internal Exception Test", "0000", "ERROR");
    }

    @Test
    void getDescription() {
        String message = "Internal Exception Test";
        String code = "0000";
        try {
            throw new InternalException(code, message, null);
        } catch (InternalException e) {
            assertEquals(message, e.getMessage());
            assertEquals(code, e.getCode());
        }
    }

    @Test
    void getDescriptionTestSucces() {
        String message = "Internal Exception Test";
        String code = "0000";
        String level = "ERROR";
        try {
            throw new InternalException(code, message,level);
        } catch (InternalException e) {
            assertEquals(code, e.getCode());
            assertEquals(level, e.getLevel());
        }
    }

    @Test
    void getExceptionType() {
        final ExceptionType exceptionType = internalException.getExceptionType();
        assertEquals(CUSTOM_EXCEPTION, exceptionType);
    }

    @Test
    void getCode() {
        final String error = internalException.getMessage();
        assertEquals("0000", error);
    }

    @Test
    void getMessage() {
        final String message = internalException.getCode();
        assertEquals("Internal Exception Test", message);
    }

    @Test
    void getExtraInfo() {
        final List<Object> list = internalException.getExtraInfo();
        assertNotNull(list);
    }

}