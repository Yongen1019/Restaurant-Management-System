package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * custom annotation: to mark which methods need to be filled with this method
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    // all properties inside annotation need to add ()
    // database operation type: UPDATE INSERT (OperationType is enumeration枚举, therefore user can only put UPDATE or INSERT)
    OperationType value();
}
