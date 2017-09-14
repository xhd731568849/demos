package com.validate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhandong on 2017/8/18/018.
 */
public interface Validate<T> {

    /**
     * 批量校验对象
     * @param list
     * @return
     * @throws IllegalAccessException
     */
    public List<TableErrorMessage> batchValidate(List<T> list) throws IllegalAccessException;

    /**
     * 校验单个对象
     * @param t
     */
    public TableErrorMessage validate(T t) throws IllegalAccessException ;
    /**
     * 校验长度
     * @param annotation
     * @param field
     * @param t
     * @return
     */
    public FieldErrorMessage validateLength(FieldMeta annotation, Field field, T t) throws IllegalAccessException ;

    /**
     * 校验是否可以为空
     * @param annotation
     * @param field
     * @param t
     * @return
     * @throws IllegalAccessException
     */
    public FieldErrorMessage validateNullable(FieldMeta annotation, Field field, T t) throws IllegalAccessException;
}
