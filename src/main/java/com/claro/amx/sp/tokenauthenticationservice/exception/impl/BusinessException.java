package com.claro.amx.sp.tokenauthenticationservice.exception.impl;

import com.claro.amx.sp.tokenauthenticationservice.exception.CustomException;

public class BusinessException extends RuntimeException implements CustomException {

    private static final long serialVersionUID = -1132348466005485433L;
    private final String message;
    private final String code;
    private final String level;

    public BusinessException(String code, String message, String level) {
        super(message);
        this.code = code;
        this.message = message;
        this.level = level;
    }



    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getLevel(){ return level; }

}