package com.huangchuan.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMQTest {
    //用于发送MQ消息
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /***
     * 消息生产测试
     */
    @Test
    public void testCreateMessage(){
        rabbitTemplate.convertAndSend("item_topic_exchange", "item.insert", "商品新增，routing key 为item.insert");
        rabbitTemplate.convertAndSend("item_topic_exchange", "item.update", "商品修改，routing key 为item.update");
        rabbitTemplate.convertAndSend("item_topic_exchange", "item.delete", "商品删除，routing key 为item.delete");
    }
}
