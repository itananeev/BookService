package messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public Queue bookQueue() {
        return QueueBuilder.durable("book-queue").build();
    }


    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("direct-exchange");
    }

    @Bean
    public Binding binding(Queue bookQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(bookQueue).to(directExchange).with("book-route");
    }

    @Bean
    public Binding searchQueueBinding(Queue searchQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(searchQueue).to(directExchange).with("search-route");
    }

}

