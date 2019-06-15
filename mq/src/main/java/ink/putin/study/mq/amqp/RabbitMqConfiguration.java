package ink.putin.study.mq.amqp;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * @author 呛水滴鱼
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class RabbitMqConfiguration {
    CachingConnectionFactory connectionFactory;
    @Bean
    public ConnectionFactory getConnectionFactory(){
        if(Objects.nonNull(connectionFactory)){
            return connectionFactory;
        }
        connectionFactory = new CachingConnectionFactory();

        connectionFactory.setVirtualHost("/");
        connectionFactory.setAddresses("172.27.6.3:5672,172.27.6.2:5672");
        connectionFactory.setUsername("rabbitmq");
        connectionFactory.setPassword("mima1234");
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate getRabbitTemplate(){
        RabbitTemplate template = new RabbitTemplate(getConnectionFactory());
        return template;
    }
}
