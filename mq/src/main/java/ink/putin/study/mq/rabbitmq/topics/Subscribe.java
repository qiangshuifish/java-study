package ink.putin.study.mq.rabbitmq.topics;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import ink.putin.study.mq.rabbitmq.ConnectionUtil;
import ink.putin.study.mq.rabbitmq.MsgConsumer;

import java.io.IOException;

/**
 * @author 呛水滴鱼
 * @version 1.0
 * @since 1.0
 */
public class Subscribe {

    private static final String EXCHANGE_NAME = "test_exchange_topic";
    private static final String QUEUE_NAME = "test_queue_topic_";

    private int prefetchCount;

    private String name;

    public Subscribe(int prefetchCount, String name) {
        this.prefetchCount = prefetchCount;
        this.name = name;
    }

    public static void main(String[] args) throws IOException {

        String routingKey1 = "user.*";
        String routingKey2 = "user.#";
        String routingKey3 = "user.list";
        String routingKey4 = "user.list.1";
        String routingKey5 = "user.list.1.10";
        String routingKey6 = "user.list.1.10";
        String routingKey7 = "user.list.1.10";

        Subscribe subscribe1 = new Subscribe(1, routingKey1);
        Subscribe subscribe2 = new Subscribe(1, routingKey2);
        Subscribe subscribe3 = new Subscribe(1, routingKey3);
        Subscribe subscribe4 = new Subscribe(1, routingKey4);
        Subscribe subscribe5 = new Subscribe(1, routingKey5);
        Subscribe subscribe6 = new Subscribe(1, routingKey6);
        Subscribe subscribe7 = new Subscribe(1, routingKey7);


        // 一个queue只会收到绑定的交换机的1条消息，消费者的优先级由 channel.basicQos(prefetchCount); 决定
        subscribe1.handMsg(QUEUE_NAME + routingKey1, routingKey1);
        subscribe2.handMsg(QUEUE_NAME + routingKey2, routingKey2);
        subscribe3.handMsg(QUEUE_NAME + routingKey3, routingKey3);
        subscribe4.handMsg(QUEUE_NAME + routingKey4, routingKey4);
        subscribe5.handMsg(QUEUE_NAME + routingKey5, routingKey5);
        subscribe6.handMsg(QUEUE_NAME + routingKey6, routingKey6);
        subscribe7.handMsg(QUEUE_NAME + routingKey7, routingKey7);

        /**
         *
         1
         ===== 消费者 # ：消费第 1 TOPIC 路由消息 user.*
         ===== 消费者 # ：消费第 2 TOPIC 路由消息 user.#
         ===== 消费者 # ：消费第 3 TOPIC 路由消息 user.list
         ===== 消费者 # ：消费第 4 TOPIC 路由消息 user.list.1
         ===== 消费者 # ：消费第 5 TOPIC 路由消息 user.list.1.10
         ===== 消费者 # ：消费第 6 TOPIC 路由消息 user.list.1.10
         2
         ===== 消费者 * ：消费第 1 TOPIC 路由消息 user.list.1.10
         3
         ===== 消费者 user.# ：消费第 1 TOPIC 路由消息 user.#
         ===== 消费者 user.# ：消费第 2 TOPIC 路由消息 user.list
         ===== 消费者 user.# ：消费第 3 TOPIC 路由消息 user.list.1.10
         4
         ===== 消费者 user.* ：消费第 1 TOPIC 路由消息 user.*
         ===== 消费者 user.* ：消费第 2 TOPIC 路由消息 user.list.1
         ===== 消费者 user.* ：消费第 3 TOPIC 路由消息 user.list.1.10
         5
         ===== 消费者 user.list ：消费第 1 TOPIC 路由消息 user.list
         6
         ===== 消费者 user.list.1 ：消费第 1 TOPIC 路由消息 user.list.1
         7
         ===== 消费者 user.list.1.10 ：消费第 1 TOPIC 路由消息 user.list.1.10
         */
    }

    public void handMsg(String queueName, String routingKey) throws IOException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //指定优先级
        channel.basicQos(prefetchCount);
        /*
         * 声明（创建）队列
         * 参数1：队列名称
         * 参数2：为true时server重启队列不会消失
         * 参数3：队列是否是独占的，如果为true只能被一个connection使用，其他连接建立时会抛出异常
         * 参数4：队列不再使用时是否自动删除（没有连接，并且没有未处理的消息)
         * 参数5：建立队列时的其他参数
         */
        channel.queueDeclare(queueName, false, false, false, null);
        /*
            绑定队列到交换机（这个交换机名称一定要和生产者的交换机名相同）
            参数1：队列名
            参数2：交换机名
            参数3：Routing key 路由键
         */
        channel.queueBind(queueName, EXCHANGE_NAME, routingKey);

        Consumer consumer = new MsgConsumer(channel, name);
        //监听队列，当b为true时，为自动提交（只要消息从队列中获取，无论消费者获取到消息后是否成功消息，都认为是消息已经成功消费），
        // 当b为false时，为手动提交（消费者从队列中获取消息后，服务器会将该消息标记为不可用状态，等待消费者的反馈，
        // 如果消费者一直没有反馈，那么该消息将一直处于不可用状态。
        //如果选用自动确认,在消费者拿走消息执行过程中出现宕机时,消息可能就会丢失！！）
        //使用channel.basicAck(envelope.getDeliveryTag(),false);进行消息确认
        channel.basicConsume(queueName, false, consumer);
    }
}
