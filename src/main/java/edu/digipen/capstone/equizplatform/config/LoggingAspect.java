package edu.digipen.capstone.equizplatform.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingAspect {

    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    public void springBeanRepositoryPointcut() {

    }

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void springBeanServicePointcut() {

    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanRestControllerPointcut() {

    }

    @Pointcut("within(@org.springframework.web.bind.annotation.ControllerAdvice *)")
    public void springBeanControllerAdvicePointcut() {

    }

    @Pointcut("springBeanRepositoryPointcut() || springBeanServicePointcut() || springBeanRestControllerPointcut() ||" +
            " springBeanControllerAdvicePointcut()")
    public void springBeanPointcut() {

    }

    @Around("springBeanPointcut()")
    public Object logMethodStartEnd(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[{}#{}] Start", joinPoint.getSignature().getDeclaringType().getSimpleName(),
                joinPoint.getSignature().getName());

        log.info("[{}#{}] End", joinPoint.getSignature().getDeclaringType().getSimpleName(),
                joinPoint.getSignature().getName());

        return joinPoint.proceed();
    }
}
