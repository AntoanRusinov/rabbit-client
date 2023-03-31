package com.example.rabbitclient;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class RabbitClientApplication {
    static final String EXCHANGE_NAME = "states";
    public static final String QUEUE_NAME = "states";

    public static void main(String[] args) {
        SpringApplication.run(RabbitClientApplication.class, args);
    }

    @Bean
    public TopicExchange statesExchange() {
        var builder = ExchangeBuilder.topicExchange(EXCHANGE_NAME);
        builder.suppressDeclaration();
        return builder.build();
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("#");
    }

    @Bean
    Queue queue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Component
    public static class Receiver {
        @RabbitListener(queues = QUEUE_NAME)
        public void receiveMessage(Message message) {
            System.out.println("Received: " + new String(message.getBody()));
        }
    }
}
