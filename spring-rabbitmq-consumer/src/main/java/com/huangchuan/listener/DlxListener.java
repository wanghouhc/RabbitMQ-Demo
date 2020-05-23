package com.huangchuan.listener;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**

 */

@Component
public class DlxListener implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //1，接收转换消息
            System.out.println(new String(message.getBody()));

            //2.处理业务逻辑
            System.out.println("处理业务逻辑...");
            int i = 3 / 0; //出现错误
            //3.成功签收
            channel.basicAck(deliveryTag, true);
        } catch (IOException e) {
            System.out.println("出现异常拒绝接收");
            //4.拒绝签收 不充回队列 queue
            //第三个参数 true：重回队列 发送消息给消费端
            channel.basicNack(deliveryTag, true, false);
//              channel.basicReject(deliveryTag, true);
        }
    }

    @Override
    public void onMessage(Message message) {

    }
}
