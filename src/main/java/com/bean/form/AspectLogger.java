package com.bean.form;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectLogger {

    private final static Logger LOG = LogManager.getLogger(AspectLogger.class);

    @Pointcut("execution(* com.bean.form.controller.*.*(..))")
    public void selectAllControllersMethods() {

    }

    @Pointcut("execution(* com.bean.form.controller.*.handleException(..))")
    public void controllersExceptions(){}

    @Before(value = "selectAllControllersMethods() && !controllersExceptions()")
    public void before(JoinPoint jp){
        StringBuffer logMessage = new StringBuffer();
        logMessage.append(jp.getTarget().getClass().getSimpleName());
        logMessage.append(".class, ");
        logMessage.append(jp.getSignature().getName());
        logMessage.append(" method has been called with argument(s): ");
        Object[] args = jp.getArgs();
        for (int i = 0; i < args.length; i++) {
            logMessage.append(args[i]).append(",");
        }
        LOG.info(logMessage.toString());
    }

    @AfterReturning(pointcut = "selectAllControllersMethods() && !controllersExceptions()", returning = "retVal")
    public void afterReturning(Object retVal){
        LOG.info("Returned value: " + retVal);
    }

    @AfterThrowing(pointcut = "selectAllControllersMethods()", throwing = "ex")
    public void afterThrowing(JoinPoint jp, Exception ex){
        LOG.error(jp.getTarget().getClass().getSimpleName()+".class, "+jp.getSignature().getName()+ " method has throwed exception "+ex.getClass().getSimpleName()+", message: " + ex.getMessage());
    }
}
