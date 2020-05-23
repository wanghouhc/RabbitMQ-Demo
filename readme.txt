day01 QuickStart
1.添加依赖

<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>5.6.0</version>
</dependency>

2.生产者配置
   --默认配置--
//创建链接工厂对象
//设置RabbitMQ服务主机地址,默认localhost
//设置RabbitMQ服务端口,默认5672
//设置虚拟主机名字，默认/
//设置用户连接名，默认guest
//设置链接密码，默认guest

---频道是对connect的细分
//创建链接
//创建频道

---队列是发送消息的通道
//声明队列
//创建消息
//消息发送
//关闭资源

3.消费者配置、
//创建链接工厂对象
//设置RabbitMQ服务主机地址,默认localhost
//设置RabbitMQ服务端口,默认5672
//设置虚拟主机名字，默认/
//设置用户连接名，默认guest
//设置链接密码，默认guest

//创建链接
//创建频道

//创建队列
//创建消费者，并设置消息处理
//消息监听
//关闭资源(不建议关闭，建议一直监听消息)

day02 Work queues工作队列模式
Work Queues与入门程序的简单模式相比，多了一个或一些消费端，多个消费端共同消费同一个队列中的消息。
在一个队列中如果有多个消费者，那么消费者之间对于同一个消息的关系是**竞争**的关系


day03  Publish/Subscribe发布与订阅模式
P：生产者，也就是要发送消息的程序，但是不再发送到队列中，而是发给X（交换机）
C：消费者，消息的接受者，会一直等待消息到来。
Queue：消息队列，接收消息、缓存消息。
Exchange：交换机，图中的X。一方面，接收生产者发送的消息。另一方面，知道如何处理消息，例如递交给某个特别队列、递交给所有队列、或是将消息丢弃。到底如何操作，取决于Exchange的类型。Exchange有常见以下3种类型：
	Fanout：广播，将消息交给所有绑定到交换机的队列
	Direct：定向，把消息交给符合指定routing key 的队列
	Topic：通配符，把消息交给符合routing pattern（路由模式） 的队列
	Exchange（交换机）只负责转发消息，不具备存储消息的能力，因此如果没有任何队列与Exchange绑定，或者没有符合路由规则的队列，那么消息会丢失！

day04 Routing路由模式
1.队列与交换机的绑定，不能是任意绑定了，而是要指定一个RoutingKey（路由key）
2.消息的发送方在 向 Exchange发送消息时，也必须指定消息的 RoutingKey。
3.Exchange不再把消息交给每一个绑定的队列，而是根据消息的Routing Key进行判断，只有队列的Routingkey与消息的 Routing key完全一致，才会接收到消息

队列与交换机绑定时只当routingKey
消息发送时只当routingKey
消息接收者只需要绑定对应的队列即可

day05 Topics通配符模式
Topic类型与Direct相比，都是可以根据RoutingKey把消息路由到不同的队列。只不过Topic类型Exchange可以让队列在绑定Routing key 的时候**使用通配符**！
Routingkey 一般都是有一个或多个单词组成，多个单词之间以”.”分割，例如： item.insert
通配符规则：
#：匹配一个或多个词
*：匹配不多不少恰好1个词
举例：
item.#：能够匹配item.insert.abc 或者 item.insert
item.*：只能匹配item.insert

channel.queueBind("topic_queue_2","topic_exchange","item.*");
通配符时使用在绑定队列和交换机 并指定路由时 对路由进行通配符的使用

----------------------------
RabbitMQ的高级特性
1 消息的可靠投递  spring-rabbitmq-provider  生产端
  confirm模式 消息从producer到exchange则会返回一个confirmCallback
  return模式 消息从producer到queue投递失败则会返回一个returnCallback

  主要特点：消息实在confirmCallback和returnCallback回调函数中发出

2. consumer ACK（acknowledge 确认）  表示消费端接收到消息后的确认  spring-rabbitmq-consumer
  2.1 自动确认 acknowledge=none
  2.2 手动确认 acknowledge=manual
  2.3 根据异常类型确认 acknowledge=auto


3.消费端限流 削峰填谷 和消费端有关 QosListener

4.死信队列  DLX  Dead Letter Exchange 死信交换机
当消息成为dead message 后，可以被重新发送到另一个交换机，这个交换机就是DLX

消息在什么情况下成为死信
1.队列消息长度到达限制
2.消费者拒绝接收消息，basicNack/basicReject，并且不把消息重新放入原目标队列 requeue=false  和消费者有关
3.原队列存在消息过期时间，消息达到超时时间未被消费

5.延迟队列
使用 TTL+死信队列组合实现延迟队列的效果

6.日志与监控

7.消息追踪
  Firehose
    1.图形界面默认交换机 ampq.rabbitmq.trace
  rabbitmq_tracing
    1.直接开启内置功能
    2.在cmd命令上开启插件
--------------------------
应用问题
1.消息可靠性保障
  消息补偿机制
2.消息幂等性保障
  乐观锁解决机制
------------------
rabbit集群搭建
1.集群搭建镜像队列
2.集群搭建 负载均衡-haproxy插件
