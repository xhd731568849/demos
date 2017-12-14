package com.xhd.mapper;

import com.xhd.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by administrator on 2017/12/14.
 */
@Mapper
public interface UserMapper {

/*    @Select("SELECT * FROM users")
    @Results({
            @Result(property = "userSex",  column = "user_sex", javaType = UserSexEnum.class),
            @Result(property = "nickName", column = "nick_name")
    })
    List<User> getAll();

    @Select("SELECT * FROM users WHERE id = #{id}")
    @Results({
            @Result(property = "userSex",  column = "user_sex", javaType = UserSexEnum.class),
            @Result(property = "nickName", column = "nick_name")
    })
    User getOne(Long id);*/

    @Insert("INSERT INTO user(name,age,phone) VALUES(#{name}, #{age}, #{phone})")
    void insert(User user);

    @Update("UPDATE user SET userName=#{userName},nick_name=#{nickName} WHERE id =#{id}")
    void update(User user);

    @Delete("DELETE FROM user WHERE id =#{id}")
    void delete(Long id);
}
