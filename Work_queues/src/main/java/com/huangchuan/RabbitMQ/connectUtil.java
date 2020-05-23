package com.huangchuan.RabbitMQ;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class connectUtil {

    /***
     * 创建链接对象
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public static Connection getConnection() throws IOException, TimeoutException {
        //创建链接工厂对象
        ConnectionFactory connectionFactory = new ConnectionFactory();

        //设置RabbitMQ服务主机地址,默认localhost
        connectionFactory.setHost("localhost");

        //设置RabbitMQ服务端口,默认5672
        connectionFactory.setPort(5672);

        //设置虚拟主机名字，默认/
        connectionFactory.setVirtualHost("/");

        //设置用户连接名，默认guest
        connectionFactory.setUsername("guest");

        //设置链接密码，默认guest
        connectionFactory.setPassword("guest");

        //创建链接
        Connection connection = connectionFactory.newConnection();
        return connection;
    }
}
