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

    /**
     * 队列名称 *-匹配单个单词  #-匹配多个单词
     */
    private static final String DIRECT_QUEUE_NAME = "ha_hello.world.queue.direct";
    private static final String TOPIC_QUEUE_NAME = "ha_hello.world.queue.topic";
    private static final String TOPIC_QUEUE_NAME_ABC = "ha_hello.world.queue.topic_abc";
    private static final String TOPIC_QUEUE_NAME_ONE_WORD = "ha_hello.world.queue.topic_*";
    private static final String TOPIC_QUEUE_NAME_MORE_WORD = "ha_hello.world.queue.topic_#.#";

    /**
     *  交换机
     */
    static final String DIRECT_EXCHANGE_NAME = "ha_hello.world.exchange.direct";
    static final String TOPIC_EXCHANGE_NAME = "ha_hello.world.exchange.topic";

    /**
     * AmqpAdmin 中定义了声明和删除的操作<br/>
     * @return
     */
    @Bean
    public AmqpAdmin getAmqpAdmin(){
        AmqpAdmin amqpAdmin = new RabbitAdmin(super.getConnectionFactory());

        amqpAdmin.deleteQueue(DIRECT_QUEUE_NAME);
        amqpAdmin.deleteQueue(TOPIC_QUEUE_NAME);
        amqpAdmin.deleteQueue(TOPIC_QUEUE_NAME_ABC);
        amqpAdmin.deleteQueue(TOPIC_QUEUE_NAME_ONE_WORD);
        amqpAdmin.deleteQueue(TOPIC_QUEUE_NAME_MORE_WORD);

        amqpAdmin.deleteExchange(DIRECT_EXCHANGE_NAME);
        amqpAdmin.deleteExchange(TOPIC_EXCHANGE_NAME);

        // 声明交换机，这些数据需要先声明好，不然生产者和消费者绑定时会报错
        amqpAdmin.declareExchange(new DirectExchange(DIRECT_EXCHANGE_NAME));
        amqpAdmin.declareExchange(new TopicExchange(TOPIC_EXCHANGE_NAME));

        // 需要声明成队列是可以重复声明的，接下来绑定才不会报错
        amqpAdmin.declareQueue(new Queue(SimpleConfiguration.DIRECT_QUEUE_NAME,true));
        amqpAdmin.declareQueue(new Queue(SimpleConfiguration.TOPIC_QUEUE_NAME,true));
        amqpAdmin.declareQueue(new Queue(SimpleConfiguration.TOPIC_QUEUE_NAME_ABC,true));
        amqpAdmin.declareQueue(new Queue(SimpleConfiguration.TOPIC_QUEUE_NAME_ONE_WORD,true));
        amqpAdmin.declareQueue(new Queue(SimpleConfiguration.TOPIC_QUEUE_NAME_MORE_WORD,true));

        // 声明绑定，声明 queue 是绑定到 exchange Binding.DestinationType.QUEUE
        // 声明绑定，声明 exchange 是绑定到 exchange Binding.DestinationType.EXCHANGE
        amqpAdmin.declareBinding(new Binding(SimpleConfiguration.DIRECT_QUEUE_NAME,Binding.DestinationType.QUEUE,SimpleConfiguration.DIRECT_EXCHANGE_NAME, "",new HashMap<>()));
        amqpAdmin.declareBinding(new Binding(SimpleConfiguration.TOPIC_QUEUE_NAME,Binding.DestinationType.QUEUE,SimpleConfiguration.DIRECT_EXCHANGE_NAME, "",new HashMap<>()));
        // 也可以使用 Builder 模式，更加直观的来指定绑定关系
        Binding abc = BindingBuilder.bind(new Queue(SimpleConfiguration.TOPIC_QUEUE_NAME_ONE_WORD, true)).to(new TopicExchange(TOPIC_EXCHANGE_NAME)).with("abc");
        amqpAdmin.declareBinding(abc);
        amqpAdmin.declareBinding(new Binding(SimpleConfiguration.TOPIC_QUEUE_NAME_ONE_WORD,Binding.DestinationType.QUEUE,SimpleConfiguration.TOPIC_EXCHANGE_NAME, "*",new HashMap<>()));
        amqpAdmin.declareBinding(new Binding(SimpleConfiguration.TOPIC_QUEUE_NAME_MORE_WORD,Binding.DestinationType.QUEUE,SimpleConfiguration.TOPIC_EXCHANGE_NAME, "#.#",new HashMap<>()));
        return amqpAdmin;
    }

    /**
     * 启动定时任务
     * @return
     */
    @Bean
    public Producer producer() {
        return new Producer();
    }

    @Bean
    public BeanPostProcessor postProcessor() {
        return new ScheduledAnnotationBeanPostProcessor();
    }


    // =====================================================consumer=====================================================

    @Bean("consumer1")
    public SimpleMessageListenerContainer listenerContainer1() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(super.getConnectionFactory());
        container.setQueueNames(DIRECT_QUEUE_NAME);
         /*
            MessageListenerAdapter 中定义了  public static final String ORIGINAL_DEFAULT_LISTENER_METHOD = "handleMessage";
            Consumer 可以选择实现 ChannelAwareMessageListener 或者 MessageListener
            或者任意一个对象，指定处理消息的方法的名称，但必须是一个 接收字符串的方法
            new MessageListenerAdapter(new Consumer()，"methodName");
            MessageConverter 为处理消息编码的装换器
         */
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
