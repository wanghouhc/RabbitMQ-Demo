package com.huangchuan.RabbitMQ;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RouteKeyProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建链接对象
        Connection connection = connectUtil.getConnection();

        //创建频道
        Channel channel = connection.createChannel();

        /**
         * 声明交换机
         * 参数1：交换机名称
         * 参数2：交换机类型，fanout、topic、direct、headers
         */
        channel.exchangeDeclare("direct_exchange", BuiltinExchangeType.DIRECT);

        /**
         * 声明队列
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare("direct_queue_insert",true,false,false,null);
        channel.queueDeclare("direct_queue_update",true,false,false,null);

        //队列绑定交换机
        channel.queueBind("direct_queue_insert","direct_exchange","insert");
        channel.queueBind("direct_queue_update","direct_exchange","update");

        //消息-direct_queue_insert
        String message_insert = "发布订阅模式-RouteKey-Insert:欢迎来到传深圳黑马训练营程序员中心！";
        /**
         * 消息发送
         * 参数1：交换机名称，如果没有指定则使用默认Default Exchage
         * 参数2：路由key,简单模式可以传递队列名称
         * 参数3：消息其它属性
         * 参数4：消息内容
         */
        channel.basicPublish("direct_exchange","insert",null,message_insert.getBytes());

        //消息-direct_queue_update
        String message_update = "发布订阅模式-RouteKey-Update:欢迎来到传深圳黑马训练营程序员中心！";
        channel.basicPublish("direct_exchange","update",null,message_update.getBytes());

        //关闭资源
        channel.close();
        connection.close();
    }
}
