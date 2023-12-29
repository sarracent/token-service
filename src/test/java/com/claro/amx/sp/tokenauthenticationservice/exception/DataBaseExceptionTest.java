package com.claro.amx.sp.tokenauthenticationservice.exception;

import com.claro.amx.sp.tokenauthenticationservice.exception.impl.DataBaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


import static com.claro.amx.sp.tokenauthenticationservice.exception.ExceptionType.CUSTOM_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DataBaseExceptionTest {
    private DataBaseException dataBaseException;

    @BeforeEach
    void setUp() {
        dataBaseException = new DataBaseException("Data Base Exception Test", "0000", "ERROR");
    }

    @Test
    void getDescriptionTest_Succes() {
        String message = "Data Base Exception Test";
        String code = "0000";
        try {
            throw new DataBaseException(code, message, null);
        } catch (DataBaseException e) {
            assertEquals(message, e.getMessage());
            assertEquals(code, e.getCode());
        }
    }

    @Test
    void getDescriptionTestSucces() {
        String message = "Data Base Exception Test";
        String code = "0000";
        String level = "ERROR";
        try {
            throw new DataBaseException(code, message, level);
        } catch (DataBaseException e) {
            assertEquals(message, e.getMessage());
            assertEquals(code, e.getCode());
        }
    }

    @Test
    void getExceptionType() {
        final ExceptionType exceptionType = dataBaseException.getExceptionType();
        assertEquals(CUSTOM_EXCEPTION, exceptionType);
    }

    @Test
    void getCode() {
        final String code = dataBaseException.getMessage();
        assertEquals("0000", code);
    }

    @Test
    void getMessage() {
        final String message = dataBaseException.getCode();
        assertEquals("Data Base Exception Test", message);
    }

    @Test
    void getExtraInfo() {
        final List<Object> list = dataBaseException.getExtraInfo();
        assertNotNull(list);
    }
}