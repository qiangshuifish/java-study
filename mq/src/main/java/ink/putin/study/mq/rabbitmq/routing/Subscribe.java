package ink.putin.study.mq.rabbitmq.routing;

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

    private static final String EXCHANGE_NAME = "test_exchange_routing";
    private static final String QUEUE_NAME = "test_queue_exchange_";

    private int prefetchCount;

    private String name;

    public Subscribe(int prefetchCount, String name) {
        this.prefetchCount = prefetchCount;
        this.name = name;
    }

    public static void main(String[] args) throws IOException {
        Subscribe subscribe1 = new Subscribe(1, "routingKey1");
        Subscribe subscribe2 = new Subscribe(1, "routingKey2");
        Subscribe subscribe3= new Subscribe(1, "routingKey3");

        subscribe1.handMsg(QUEUE_NAME+"1","1");
        subscribe2.handMsg(QUEUE_NAME+"2","2");
        subscribe3.handMsg(QUEUE_NAME+"3","3");
    }

    public void handMsg(String queueName,String routingKey) throws IOException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //能者多劳模式
        channel.basicQos(prefetchCount);
        //声明队列
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
