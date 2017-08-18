package com.validate;

/**
 * Created by xuhandong on 2017/8/17/017.
 */
public class People {
    @FieldMeta(unionKey = true)
    private String name;
    @FieldMeta(length = 14,lengthType = LengthType.LESS_THAN_EQUAL_TO)
    private Integer id;
    @FieldMeta(nullable = false , name = "身份证号" , length = 18 , lengthType = LengthType.EQUAL_TO)
    private String idCard;
    @FieldMeta(nullable = false , name = "年龄" , length = 3,lengthType = LengthType.LESS_THAN_EQUAL_TO)
    private Integer age;
    @FieldMeta(nullable = false , name = "性别" ,values = {"男","女"})
    private String sex;
    @FieldMeta(nullable = false , name = "地址" , length = 18,lengthType = LengthType.LESS_THAN_EQUAL_TO)
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
