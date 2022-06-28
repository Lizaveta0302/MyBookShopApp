package com.example.bookshop_app.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class MethodExecDurationTrackerAspect {

    private static final Logger logger = LoggerFactory.getLogger(MethodExecDurationTrackerAspect.class);

    @Around(value = "@annotation(com.example.bookshop_app.aop.annotation.DurationTrackable))")
    public Object aroundDurationTrackingAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        long durationMills = new Date().getTime();
        logger.info("{} duration tracking begins", proceedingJoinPoint);
        Object returnValue = null;
        try {
            returnValue = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        durationMills = new Date().getTime() - durationMills;
        logger.info("{} method execution took: {} millis", proceedingJoinPoint, durationMills);
        return returnValue;
    }
}