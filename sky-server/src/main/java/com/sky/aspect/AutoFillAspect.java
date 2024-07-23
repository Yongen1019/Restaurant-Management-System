package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * custom AOP: implement AutoFill logic
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    // execution(returnType package.class.method(param))
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {}

    /**
     * Before: will execute this method when autoFillPointCut matched
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("start auto fill");

        // get OperationType value of the method being intercepted
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // through method signature, we can get the details of the join point, ex. param type, return type, annotation
        AutoFill autofill = signature.getMethod().getAnnotation(AutoFill.class); // get method annotation object
        OperationType operationType = autofill.value(); // get the value inside method annotation object [database operation type]

        // get the param - Entity object (ex. Employee) of the method being intercepted
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }

        Object entity = args[0]; // don't use Employee employee since the parameter type not necessarily is Employee

        // set value to Employee object properties
        // if insert
        if (operationType == OperationType.INSERT) {
            // use setter method of entity by reflection
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);

                setCreateTime.invoke(entity, LocalDateTime.now());
                setCreateUser.invoke(entity, BaseContext.getCurrentId()); // base context having a thread local that storing the current logged user id
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        // if insert or update
        // use setter method of entity by reflection
        try {
            // use constant to avoid we type the incorrect method name
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

            setUpdateTime.invoke(entity, LocalDateTime.now());
            setUpdateUser.invoke(entity, BaseContext.getCurrentId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
