package ink.putin.study.mq.amqp.simple;

import ink.putin.study.mq.amqp.RabbitMqConfiguration;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

import java.util.HashMap;

/**
 * @author 呛水滴鱼
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class SimpleConfiguration extends RabbitMqConfiguration {


    public static final String DIRECT_QUEUE_NAME = "ha_hello.world.queue.direct";
    public static final String TOPIC_QUEUE_NAME_ABC = "ha_hello.world.queue.topic_abc";
    public static final String TOPIC_QUEUE_NAME_ONE_WORD = "ha_hello.world.queue.topic_*";
    public static final String TOPIC_QUEUE_NAME_MORE_WORD = "ha_hello.world.queue.topic_#.#";




    public static final String DIRECT_EXCHANGE_NAME = "ha_hello.world.exchange.direct";
    public static final String TOPIC_EXCHANGE_NAME = "ha_hello.world.exchange.topic";

    @Bean
    public AmqpAdmin getAmqpAdmin(){
        AmqpAdmin amqpAdmin = new RabbitAdmin(super.getConnectionFactory());

        amqpAdmin.deleteQueue(DIRECT_QUEUE_NAME);
        amqpAdmin.deleteQueue(TOPIC_QUEUE_NAME_ABC);
        amqpAdmin.deleteQueue(TOPIC_QUEUE_NAME_ONE_WORD);
        amqpAdmin.deleteQueue(TOPIC_QUEUE_NAME_MORE_WORD);

        amqpAdmin.deleteExchange(DIRECT_EXCHANGE_NAME);
        amqpAdmin.deleteExchange(TOPIC_EXCHANGE_NAME);

        amqpAdmin.declareExchange(new DirectExchange(DIRECT_EXCHANGE_NAME));
        amqpAdmin.declareExchange(new TopicExchange(TOPIC_EXCHANGE_NAME));

        amqpAdmin.declareQueue(new Queue(SimpleConfiguration.DIRECT_QUEUE_NAME,true));
        amqpAdmin.declareQueue(new Queue(SimpleConfiguration.TOPIC_QUEUE_NAME_ABC,true));
        amqpAdmin.declareQueue(new Queue(SimpleConfiguration.TOPIC_QUEUE_NAME_ONE_WORD,true));
        amqpAdmin.declareQueue(new Queue(SimpleConfiguration.TOPIC_QUEUE_NAME_MORE_WORD,true));

        amqpAdmin.declareBinding(new Binding(SimpleConfiguration.DIRECT_QUEUE_NAME,Binding.DestinationType.QUEUE,SimpleConfiguration.DIRECT_EXCHANGE_NAME, ExchangeTypes.DIRECT,new HashMap<>()));
        amqpAdmin.declareBinding(new Binding(SimpleConfiguration.TOPIC_QUEUE_NAME_ABC,Binding.DestinationType.QUEUE,SimpleConfiguration.TOPIC_EXCHANGE_NAME, "abc",new HashMap<>()));
        amqpAdmin.declareBinding(new Binding(SimpleConfiguration.TOPIC_QUEUE_NAME_ONE_WORD,Binding.DestinationType.QUEUE,SimpleConfiguration.TOPIC_EXCHANGE_NAME, "*",new HashMap<>()));
        amqpAdmin.declareBinding(new Binding(SimpleConfiguration.TOPIC_QUEUE_NAME_MORE_WORD,Binding.DestinationType.QUEUE,SimpleConfiguration.TOPIC_EXCHANGE_NAME, "#.#",new HashMap<>()));
        return amqpAdmin;
    }

    @Bean
    public Producer scheduledProducer() {
        return new Producer();
    }

    @Bean
    public BeanPostProcessor postProcessor() {
        return new ScheduledAnnotationBeanPostProcessor();
    }


    // ===============================consumer===========================================================================

    @Bean("consumer1")
    public SimpleMessageListenerContainer listenerContainer1() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(super.getConnectionFactory());
        container.setQueueNames(DIRECT_QUEUE_NAME);
        container.setMessageListener(new MessageListenerAdapter(new Consumer("",DIRECT_QUEUE_NAME,"")));
        return container;
    }

    @Bean("consumer2")
    public SimpleMessageListenerContainer listenerContainer2() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(super.getConnectionFactory());
        String routingKey = "abc";
        container.setLookupKeyQualifier(routingKey);
        container.setQueueNames(TOPIC_QUEUE_NAME_ABC);
        container.setMessageListener(new MessageListenerAdapter(new Consumer(TOPIC_EXCHANGE_NAME,TOPIC_QUEUE_NAME_ABC, routingKey)));
        return container;
    }

    @Bean("consumer3")
    public SimpleMessageListenerContainer listenerContainer3() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(super.getConnectionFactory());
        String routingKey = "#.#";
        container.setLookupKeyQualifier(routingKey);
        container.setQueueNames(TOPIC_QUEUE_NAME_MORE_WORD);
        container.setMessageListener(new MessageListenerAdapter(new Consumer(TOPIC_EXCHANGE_NAME,TOPIC_QUEUE_NAME_MORE_WORD, routingKey)));
        return container;
    }

    @Bean("consumer4")
    public SimpleMessageListenerContainer listenerContainer4() {
        String routingKey = "*";
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(super.getConnectionFactory());
        container.setLookupKeyQualifier(routingKey);
        container.setQueueNames(TOPIC_QUEUE_NAME_ONE_WORD);
        container.setMessageListener(new MessageListenerAdapter(new Consumer(TOPIC_EXCHANGE_NAME,TOPIC_QUEUE_NAME_ONE_WORD,routingKey)));
        return container;
    }



}
