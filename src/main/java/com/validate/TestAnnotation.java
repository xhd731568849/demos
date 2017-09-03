package com.validate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhandong on 2017/8/17/017.
 */
public class TestAnnotation {

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);
        System.out.println(list.size());
    }

    public static void d(String[] args) throws IllegalAccessException {
        People people = new People();
        people.setId(1);
        people.setAge(18);
        people.setSex(1);
        people.setIdCard("13063419951121001");
        //people.setAddress("草桥东路");
        People people1 = new People();
        people1.setId(12);
        people1.setAge(18);
        people1.setSex(22);
        people1.setIdCard("130634199511210015");
        people1.setAddress("草桥东路");
        List<People> list = new ArrayList<People>();
        list.add(people);
        list.add(people1);
        List<TableErrorMessage> tableErrorMessages = new Validate<People>().batchValidate(list);
        if(tableErrorMessages!=null && tableErrorMessages.size()>0) {
            for (TableErrorMessage tableErrorMessage : tableErrorMessages) {
                if(tableErrorMessage != null) {
                    for (FieldErrorMessage fieldErrorMessage:tableErrorMessage.getFieldErrorMessages()){
                        if(fieldErrorMessage!=null){
                            System.out.println(tableErrorMessage.getTableId()+"   "+fieldErrorMessage.getMsg());
                        }
                    }
                }
            }
        }
    }
}
