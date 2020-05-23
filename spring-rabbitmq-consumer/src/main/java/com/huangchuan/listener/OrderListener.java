package com.huangchuan.listener;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**

 */

@Component
public class OrderListener implements ChannelAwareMessageListener {



    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //1，接收转换消息
            System.out.println(new String(message.getBody()));

            //2.处理业务逻辑
            System.out.println("处理业务逻辑...");
            System.out.println("根据订单id查询其状态。。。。。");
            System.out.println("判断是否支付成功");
            System.out.println("取消订单回滚库存。。。。");
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
