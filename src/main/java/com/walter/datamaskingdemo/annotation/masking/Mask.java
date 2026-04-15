package com.walter.datamaskingdemo.annotation.masking;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mask {
    int prefix() default 0;      // Show at start
    int suffix() default 0;      // Show at end
    char maskChar() default '*'; // Character to use
}
