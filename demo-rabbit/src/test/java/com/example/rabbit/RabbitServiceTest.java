package com.example.rabbit;

import com.example.rabbit.service.RabbitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author tanzhibo
 * @date 2017/12/13 14:23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitServiceTest {
    @Autowired
    private RabbitService sender;
    @Test
    public void fanoutSender() throws Exception {
        sender.send("test","test");
    }
}