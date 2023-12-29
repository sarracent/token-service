package com.claro.amx.sp.tokenauthenticationservice.service;

import java.util.function.Supplier;

public interface Resilience4jService {
    <T> T executeGetToken(Supplier<T> operation);
}
