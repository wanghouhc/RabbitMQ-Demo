package com.huangchuan.listener;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * consumer 限流机制
 *  1.确保consumer的ack机制为手动确认
 *  2.Listener-container配置属性 perfetch=1  表示消费端每次从mq拉取一条消息，知道手动确认消费完毕后，才会去拉取下一条
 */

@Component
public class QosListener implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        Thread.sleep(1000);
        //1.获取消息
        System.out.println(new String(message.getBody()));

        //2.处理业务逻辑

        //3.签收
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
    }

    @Override
    public void onMessage(Message message) {

    }
}
