package com.huangchuan.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class RabbitMQTest {
    //用于发送MQ消息
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 确认模式
     * 步骤：
     * 1。确认模式开启 在connectionFactory 中定义 publisher-confirms="true"
     * 2.在rabbitmqTemplate定义confirmCallBack回调函数
     */
    @Test
    public void testconfirm() {


        //2，定义回调函数
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData 相关配置信息
             * @param ack exchange是否成功收到消息 收到 true 没收到false
             * @param s  失败原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String s) {
                System.out.println("confirm方法被执行");
                if (ack) {
                    System.out.println("success: " + s);
                } else {
                    System.out.println("false: " + s);
                    //做一些处理，消息再次发送
                }
            }
        });
        //发送消息
        rabbitTemplate.convertAndSend("test_exchange_confirm",
                "confirm", "message confirm");
    }

    /**
     * 消息回退模式：当消息发送给exchange后，exchange发送给Queue失败后 才会执行 returnback
     * 步骤：
     * 1.开启回退模式  publisher-returns="true"
     * 2.设置returnCallback
     * 3.设置Exchange处理消息的模式
     * 3.1 如果消息没有路由到queue，则丢弃  默认
     * 3.2                      返回给消息发送方returnBack
     */
    @Test
    public void testReturn() {
        //1.设置交换机处理失败的模式
        rabbitTemplate.setMandatory(true);

        //2，定义回调函数
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             *
             * @param message 消息对象
             * @param i 失败错误码
             * @param s 失败信息
             * @param s1  交换机
             * @param s2 路由key
             */
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                System.out.println("return 执行。。。。。");
                System.out.println(message);
                System.out.println(i);
                System.out.println(s);
                System.out.println(s1);
                System.out.println(s2);
            }
        });
        //发送消息
        rabbitTemplate.convertAndSend("test_exchange_confirm",
                "confir", "message confirm");
    }

    /**
     * 发消息
     */
    @Test
    public void send() {
        for (int i = 0; i < 10; i++) {
            //发送消息
            rabbitTemplate.convertAndSend("test_exchange_confirm",
                    "confirm", "message confirm");
        }

    }

    /**
     * 发送测试死信队列
     * 1.队列消息长度到达限制
     * 2.消费者拒绝接收消息，
     * 3.原队列存在消息过期时间，消息达到超时时间未被消费
     */
    @Test
    public void testDlx() {
        //1.测试过期时间的死信消息
//        rabbitTemplate.convertAndSend("test_exchange_dlx",
//                "test.dlx.haha", "message dlx");

        //2.测试长度
//        for (int i = 0; i <20 ; i++) {
//            rabbitTemplate.convertAndSend("test_exchange_dlx",
//                    "test.dlx.haha", "message dlx");
//        }

        //3.测试消息拒收
        rabbitTemplate.convertAndSend("test_exchange_dlx",
                "test.dlx.haha", "message........dlx");

    }


    @Test
    public void testDelay() throws InterruptedException {
        //1.发送订单消息 将来是在订单系统中 下单成功后 发送消息
        rabbitTemplate.convertAndSend("order_exchange",
                "order.msg", " 订单消息");

        //2.打印倒计时
        for (int i = 10; i > 0; i--) {
            System.out.println(i + "..");
            Thread.sleep(1000);
        }
    }
}
