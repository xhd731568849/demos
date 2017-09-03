package com.validate;

import java.lang.annotation.*;

/**
 * Created by xuhandong on 2017/8/17/017.
 */
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.FIELD,ElementType.METHOD})//定义注解的作用目标**作用范围字段、枚举的常量/方法
@Documented//说明该注解将被包含在javadoc中
public @interface FieldMeta {

    String[] values() default "";
    /**
     * 标示唯一的一行
     * @return
     */
    boolean unionKey() default false;

    /**
     * 是否可以为空
     * @return
     */
    boolean nullable() default false;
    /**
     * 字段名称
     * @return
     */
    String name() default "";
    /**
     * 字段类型
     * @return
     */
    String fieldType() default "String";

    /**
     * 长度
     * @return
     */
    int length() default 0;

    /**
     * 长度类型,等于,小于等于
     * @return
     */
    int lengthType() default LengthType.LESS_THAN_EQUAL_TO;

}