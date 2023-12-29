package com.claro.amx.sp.tokenauthenticationservice.exception;

import java.util.Collections;
import java.util.List;

public interface CustomException {
    String getMessage();

    String getCode();

    String getLevel();

    default ExceptionType getExceptionType() {
        return ExceptionType.CUSTOM_EXCEPTION;
    }

    default List<Object> getExtraInfo() {
        return Collections.emptyList();
    }

}
