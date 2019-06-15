package ink.putin.study.mq.amqp.simple;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import static ink.putin.study.mq.amqp.simple.SimpleConfiguration.*;

/**
 * @author 呛水滴鱼
 * @version 1.0
 * @since 1.0
 */
@Component
public class Producer {
    @Resource
    private volatile RabbitTemplate rabbitTemplate;
    private AtomicInteger count = new AtomicInteger(0);
    @Scheduled(fixedRate = 5000)
    public void sendMessage() {
        System.out.println("===============================生产者==============================");
        String message = "我是消费者生成的消息 " + count.incrementAndGet();
        Random random = new Random();

        if(random.nextInt(2) >= 1){
            // 发送 DIRECT
            rabbitTemplate.convertAndSend(DIRECT_EXCHANGE_NAME, ExchangeTypes.DIRECT,message);
        }else{
            // 发送 TOPIC
            rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_NAME, "abc",message+"_abc");
            rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_NAME, "abc.efg",message+"_abc.efg");
            rabbitTemplate.convertAndSend(TOPIC_EXCHANGE_NAME, "abc.123.456",message+"_abc.123.456");
        }
    }

}
