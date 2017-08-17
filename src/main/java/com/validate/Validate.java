package com.validate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhandong on 2017/8/17/017.
 */
public class Validate<T> {

    /**
     * 批量校验对象
     * @param list
     * @return
     * @throws IllegalAccessException
     */
    public List<TableErrorMessage> batchValidate(List<T> list) throws IllegalAccessException {
        List<TableErrorMessage> tableErrorMessages = new ArrayList<TableErrorMessage>();
        for(T t : list){
            TableErrorMessage tableErrorMessage = validate(t);
            tableErrorMessages.add(tableErrorMessage);
        }
        return tableErrorMessages;
    }

    /**
     * 校验单个对象
     * @param t
     */
    public TableErrorMessage validate(T t) throws IllegalAccessException {
        Class<?> clazz = t.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        List<FieldErrorMessage> errorMessages = new ArrayList<FieldErrorMessage>();
        Integer id = null;
        for(Field field : declaredFields){
            field.setAccessible(true);
            FieldMeta annotation = field.getAnnotation(FieldMeta.class);
            if(annotation != null) {
                //字段上有注解,开始校验
                FieldErrorMessage errorMessage = validateNullable(annotation, field, t);
                if(errorMessage != null){
                    errorMessages.add(errorMessage);
                }
                FieldErrorMessage errorMessage1 = validateLength(annotation, field, t);
                if(errorMessage1 != null) {
                    errorMessages.add(errorMessage1);
                }
                if(annotation.id() == true){
                    id = (Integer) field.get(t);
                }
            }
        }
        TableErrorMessage tableErrorMessage = new TableErrorMessage();
        tableErrorMessage.setFieldErrorMessages(errorMessages);
        tableErrorMessage.setTableId(id.toString());
        return tableErrorMessage;
    }

    /**
     * 校验长度
     * @param annotation
     * @param field
     * @param t
     * @return
     */
    private FieldErrorMessage validateLength(FieldMeta annotation, Field field, T t) throws IllegalAccessException {
        Object o = field.get(t);
        if(o == null){
            //如果是空,就不用校验长度了
            return null;
        }
        String obj = null;
        if(field.getType() == Integer.class){
            obj = ((Integer) o).toString();
        }else if(field.getType() ==String.class){
            obj = (String) o;
        }

        if(annotation.lengthType() == LenthType.EQUAL_TO){
            //如果是只能等于该长度
            if(obj.length() != annotation.length()){
                return new FieldErrorMessage(field.getName()+"长度不符!");
            }
        }else if(annotation.lengthType() == LenthType.LESS_THAN_EQUAL_TO){
            //小于等于该长度
            if(obj.length() > annotation.length()){
                return new FieldErrorMessage(field.getName()+"长度不符!");
            }
        }
        return null;
    }

    /**
     * 校验是否可以为空
     * @param annotation
     * @param field
     * @param t
     * @return
     * @throws IllegalAccessException
     */
    private FieldErrorMessage validateNullable(FieldMeta annotation, Field field, T t) throws IllegalAccessException {
        if (annotation.nullable() == false){
            if(field.get(t) == null){
                return new FieldErrorMessage(field.getName()+"不能为空!");
            }
        }
        return null;
    }
}
