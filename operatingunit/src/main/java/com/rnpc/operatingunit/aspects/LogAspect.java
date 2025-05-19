package com.rnpc.operatingunit.aspects;

import com.rnpc.operatingunit.model.LogMethodExecution;
import com.rnpc.operatingunit.service.impl.LogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {
    private final LogService logService;

    @Pointcut("@annotation(logMethodExecution)")
    public void annotatedMethods(LogMethodExecution logMethodExecution) {
    }

    @AfterReturning(pointcut = "annotatedMethods(logMethodExecution)", argNames = "logMethodExecution")
    public void logAfterSuccess(LogMethodExecution logMethodExecution) {
        logService.createLog(logMethodExecution.entity(), logMethodExecution.operation());
    }
}
