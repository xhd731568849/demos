package com.validate;

/**
 * Created by xuhandong on 2017/8/17/017.
 */
public class People {
    @FieldMeta(id = true,length = 14,lengthType = LenthType.LESS_THAN_EQUAL_TO)
    private Integer id;
    @FieldMeta(nullable = false , name = "身份证号" , length = 18 , lengthType = LenthType.EQUAL_TO)
    private String idCard;
    @FieldMeta(nullable = false , name = "年龄" , length = 3,lengthType = LenthType.LESS_THAN_EQUAL_TO)
    private Integer age;
    @FieldMeta(nullable = false , name = "性别" , length = 1,lengthType = LenthType.EQUAL_TO)
    private Integer sex;
    @FieldMeta(nullable = false , name = "地址" , length = 18,lengthType = LenthType.LESS_THAN_EQUAL_TO)
    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
