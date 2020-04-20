package com.baizhi.wdx.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//在方法上使用
@Target({ElementType.METHOD})
//在运行时生效
@Retention(RetentionPolicy.RUNTIME)
public @interface AddLog {

    String value();

}
