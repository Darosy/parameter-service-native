package id.co.bni.parameter.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Slf4j
public class AspectConfig {
    static final String METHOD = " in method ";

    @Around("execution(* id.co.bni.parameter.services..*(..))")
    public Object interceptorForServices(ProceedingJoinPoint joinPoint) throws Throwable {
        return startStopWatch(joinPoint, " Services ");
    }

    @Around("execution(* id.co.bni.parameter.controller..*(..))")
    public Object interceptorForControllers(ProceedingJoinPoint joinPoint) throws Throwable {
        return startStopWatch(joinPoint, " Controller ");
    }

    @Around("execution(* id.co.bni.parameter.repository..*(..))")
    public Object interceptorForRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        return startStopWatch(joinPoint, " Repository ");
    }

    private Object startStopWatch(ProceedingJoinPoint joinPoint, String desc) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        try {
            stopWatch.start();
            log.info("Start" + desc + className + METHOD + methodName);
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            log.info("End" + desc + className + METHOD + methodName + " " + stopWatch.getTotalTimeMillis() + " ms");
        }
    }
}
