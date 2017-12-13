package com.example.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author tanzhibo
 * @date 2017/12/13 14:18.
 */
@Component
public class FanoutReceiverA {
    @RabbitListener(queues = "fanout.A")
    @RabbitHandler
    public void process(String message) {
        System.out.println("fanout Receiver A : " + message);
    }
}