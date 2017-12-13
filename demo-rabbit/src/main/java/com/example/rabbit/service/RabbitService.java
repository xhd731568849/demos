package com.example.rabbit.service;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author tanzhibo
 * @date 2017/12/13 14:19.
 */
@Component
public class RabbitService {
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private RabbitAdmin rabbitAdmin;

    public void send(String exchange, String msg) {
        System.out.println("Sender : " + msg);
        this.rabbitTemplate.convertAndSend(exchange,"", msg);
    }

    public void addFanoutExchange(String name){
        FanoutExchange fanout = new FanoutExchange(name);
        rabbitAdmin.declareExchange(fanout);
    }

    public boolean delExchange(String name){
        return rabbitAdmin.deleteExchange(name);
    }

    public void addQueue(String name){
        Queue queue = new Queue(name);
        rabbitAdmin.declareQueue(queue);
    }

    public void delQueue(String name){
        rabbitAdmin.deleteQueue(name);
    }

    public void bind(String queue,String fanoutExchange){
        Binding binding = BindingBuilder.bind(new Queue(queue)).to(new FanoutExchange(fanoutExchange));
        rabbitAdmin.declareBinding(binding);
    }

    public void removeBinding(String queue,String fanoutExchange){
        Binding binding = BindingBuilder.bind(new Queue(queue)).to(new FanoutExchange(fanoutExchange));
        rabbitAdmin.removeBinding(binding);
    }
}
