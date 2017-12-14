package com.example.rabbit.entity;

import com.btzh.mis.core.dao.annotation.Table;

import java.io.Serializable;

/**
 * @author xhd
 * @date 2017/12/14
 */
@Table(table = "user",id = "id",idField = "id",sequence = "SEQ_user")
public class User implements Serializable {

    private Integer id;
    private String name;
    private Integer age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
