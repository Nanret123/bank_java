package com.example.bank.audit.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.bank.audit.enums.OperationType;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {
    String module() default "";
    OperationType operation() default OperationType.UPDATE;
    String entityType() default "";
    boolean captureArgs() default true;
    boolean captureResult() default true;
    String[] sensitiveFields() default {};
}