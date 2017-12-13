package com.example.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tanzhibo
 * @date 2017/12/13 13:55.
 */
@Configuration
public class RabbitConfig {
    public static final String EXCHANGE   = "spring-fanout-exchange";

    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
    /*@Bean
    public CachingConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("192.168.0.102:5672");
        connectionFactory.setUsername("user");
        connectionFactory.setPassword("password");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true); //必须要设置
        return connectionFactory;
    }*/
    //创建队列
    @Bean
    public Queue AMessage() {
        return new Queue("fanout.A", true);
    }
    //创建队列
    @Bean
    public Queue BMessage() {
        return new Queue("fanout.B", true);
    }
    //创建队列
    @Bean
    public Queue CMessage() {
        return new Queue("fanout.C", true);
    }
    //创建Fanout交换器
    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE);
    }
    //将对列绑定到Fanout交换器
    @Bean
    Binding bindingExchangeA(Queue AMessage,FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(AMessage).to(fanoutExchange);
    }
    //将对列绑定到Fanout交换器
    @Bean
    Binding bindingExchangeB(Queue BMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(BMessage).to(fanoutExchange);
    }
    //将对列绑定到Fanout交换器
    @Bean
    Binding bindingExchangeC(Queue CMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(CMessage).to(fanoutExchange);
    }
}
