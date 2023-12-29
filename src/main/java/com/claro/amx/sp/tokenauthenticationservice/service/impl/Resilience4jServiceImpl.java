package com.claro.amx.sp.tokenauthenticationservice.service.impl;


import com.claro.amx.sp.tokenauthenticationservice.service.Resilience4jService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class Resilience4jServiceImpl implements Resilience4jService {

    private static final String TOKEN_API = "token";

    @Override
    @CircuitBreaker(name = TOKEN_API)
    @RateLimiter(name = TOKEN_API)
    @Bulkhead(name = TOKEN_API)
    @Retry(name = TOKEN_API)
    public <T> T executeGetToken(Supplier<T> operation) {
        return operation.get();
    }

}
