package com.claro.amx.sp.tokenauthenticationservice.annotations.auditable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface AuditableParamIgnore {
    String nameToAudit() default "";

    Class<?> type() default Void.class;
}
