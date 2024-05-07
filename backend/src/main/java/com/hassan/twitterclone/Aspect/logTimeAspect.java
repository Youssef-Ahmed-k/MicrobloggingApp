package com.hassan.twitterclone.Aspect;

import org.apache.commons.lang3.StringUtils; // Import Apache Commons Lang StringUtils

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@EnableAspectJAutoProxy
@Aspect
@Component
public class logTimeAspect {

    Logger log = LoggerFactory.getLogger(logTimeAspect.class);

    @Around(value = "execution(* com.hassan.twitterclone.service..*(..))")
    public Object LogTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Long startTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder("KPI:");
        sb.append("[").append(joinPoint.getKind()).append("]\tfor: ")
                .append(joinPoint.getSignature())
                .append("\twithArgs: ").append("(")
                .append(StringUtils.join(joinPoint.getArgs(), ",")).append(")"); // Use Apache Commons Lang StringUtils

        Object returnValue = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        sb.append("\ttook ").append(endTime - startTime).append(" ms.");
        log.info(sb.toString());

        return returnValue;
    }
}