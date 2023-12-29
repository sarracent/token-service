package com.claro.amx.sp.tokenauthenticationservice.exception.impl;

import com.claro.amx.sp.tokenauthenticationservice.exception.CustomException;

public class ControllersException extends RuntimeException implements CustomException {

    private static final long serialVersionUID = -1132348466005485433L;
    private final String message;
    private final String code;
    private final String level;

    public ControllersException(String code, String message, String level) {
        super(message);
        this.message = message;
        this.code = code;
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
    public String getLevel(){return level;}

}