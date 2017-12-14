package com.example.rabbit.dao;

import com.example.rabbit.datasource.DataSource;
import com.example.rabbit.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @author xhd
 * @date 2017/12/14
 */
@Repository
public class UserDAOImpl extends DataSource<User> implements UserDAO {
}
