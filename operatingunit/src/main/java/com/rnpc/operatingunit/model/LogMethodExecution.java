package com.rnpc.operatingunit.model;

import com.rnpc.operatingunit.enums.LogAffectedEntityType;
import com.rnpc.operatingunit.enums.LogOperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogMethodExecution {
    LogAffectedEntityType entity() default LogAffectedEntityType.USER;
    LogOperationType operation() default LogOperationType.CREATE;
}
