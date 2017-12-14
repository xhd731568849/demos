package com.example.rabbit.service;

import com.example.rabbit.dao.UserDAO;
import com.example.rabbit.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

/**
 * @author xhd
 * @date 2017/12/14
 */
@Service
public class MyService {
    @Autowired
    private UserDAO userDAO;
    public void test1() {
        User user = new User();
        user.setAge(new Random().nextInt(10));
        user.setName(UUID.randomUUID().toString());
        userDAO.add(user);
    }
}
