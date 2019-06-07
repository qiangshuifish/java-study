package ink.putin.study.mq.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 呛水滴鱼
 * @version 1.0
 * @since 1.0
 */
public class MsgConsumer extends DefaultConsumer {

    private String name;

    private AtomicInteger count;

    public MsgConsumer(Channel channel, String name) {
        super(channel);
        this.name = name;
        count = new AtomicInteger(0);
    }

    public MsgConsumer(Channel channel) {
        super(channel);
        this.name = getClass().getSimpleName();
        count = new AtomicInteger(0);
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void handleConsumeOk(String consumerTag) {
        super.handleConsumeOk(consumerTag);
    }

    @Override
    public void handleCancelOk(String consumerTag) {
        super.handleCancelOk(consumerTag);
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
        super.handleCancel(consumerTag);
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        super.handleShutdownSignal(consumerTag, sig);
    }

    @Override
    public void handleRecoverOk(String consumerTag) {
        super.handleRecoverOk(consumerTag);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "utf-8");
        System.out.printf("===== 消费者 %s ：消费第 %s 条 ==== %s \n",name,count.incrementAndGet(),message);
        this.getChannel().basicAck(envelope.getDeliveryTag(),false);
    }

    @Override
    public Channel getChannel() {
        return super.getChannel();
    }

    @Override
    public String getConsumerTag() {
        return super.getConsumerTag();
    }
}
