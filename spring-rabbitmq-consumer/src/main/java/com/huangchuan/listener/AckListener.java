package com.huangchuan.listener;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * consumer ACK机制
 * 1.设置手动签收 <rabbit:listener-container connection-factory="connectionFactory" acknowledge="manual">
 * 2.让监听器实现ChannelAwareMessageListener接口
 * 3.如果消息成功处理，则调用channel的basicAck()签收
 * 3.如果消息成功失败，则调用channel的basicNock()拒绝签收，重新发送给consumer
 */

@Component
public class AckListener implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //1，接收转换消息
            System.out.println(new String(message.getBody()));

            //2.处理业务逻辑
            System.out.println("处理业务逻辑...");

            //3.成功签收
            channel.basicAck(deliveryTag, true);
        } catch (IOException e) {
            //4.拒绝签收
            //第三个参数 true：重回队列 发送消息给消费端
//            channel.basicNack(deliveryTag,true,true);
//            channel.basicNack(deliveryTag, true, true);
            channel.basicReject(deliveryTag, true);
        }
    }

    @Override
    public void onMessage(Message message) {

    }
}
