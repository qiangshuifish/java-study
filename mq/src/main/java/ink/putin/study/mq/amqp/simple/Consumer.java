package ink.putin.study.mq.amqp.simple;

/**
 * @author 呛水滴鱼
 * @version 1.0
 * @since 1.0
 */

public class Consumer {

    private String exchange;
    private String queueName;
    private String bindName;

    public Consumer(String exchange, String queueName, String bindName) {
        this.exchange = exchange;
        this.queueName = queueName;
        this.bindName = bindName;
    }

    public void handleMessage(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("====").append(exchange).append("====")
                .append(queueName).append("====").append(bindName);
        sb.append("\n").append("消息：").append(message).append("\n");
        System.out.println(sb.toString());
    }
}
