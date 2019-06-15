package ink.putin.study.mq.amqp.simple;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author 呛水滴鱼
 * @version 1.0
 * @since 1.0
 */
public class TestSimple {
    public static void main(String[] args) throws InterruptedException {
        new AnnotationConfigApplicationContext(SimpleConfiguration.class);
    }
}
