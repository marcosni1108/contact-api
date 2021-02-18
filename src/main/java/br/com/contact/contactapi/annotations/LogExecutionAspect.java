package br.com.contact.contactapi.annotations;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Bean to log execution of method annotated with
 * {@link br.com.contact.contactapi.annotations.LogExecution}
 *
 * @author Marcos Silva
 */
@Slf4j
@Aspect
public class LogExecutionAspect {

    @Around( "@annotation(br.com.contact.contactapi.annotations.LogExecution)" )
    public Object logExecution( ProceedingJoinPoint joinPoint ) throws Throwable {

        String declaringTypeName = joinPoint.getSignature()
                .getDeclaringTypeName();
        String name = joinPoint.getSignature()
                .getName();

        log.debug( "Running: [{}.{}] Args[{}]", declaringTypeName, name, joinPoint.getArgs() );
        try {
            return joinPoint.proceed();
        } finally {
            log.debug( "Finishing: [{}.{}] Args[{}]", declaringTypeName, name, joinPoint.getArgs() );
        }
    }

}
