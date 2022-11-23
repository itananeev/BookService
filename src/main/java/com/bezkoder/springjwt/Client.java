package com.bezkoder.springjwt;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class Client {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DirectExchange directExchange;

    public Object sendMessageAndReceiveResponse(String message, String routingKey) {
        Object response = (Object) rabbitTemplate.convertSendAndReceive(directExchange.getName(), routingKey, message);
        System.out.println("Routing Key: "+ routingKey + " Response: " + response);
        return response;
    }

}