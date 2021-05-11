package com.imploded.rabbit.configuration;

import com.imploded.rabbit.service.RabbitConfirmCallbackService;
import com.imploded.rabbit.service.RabbitReturnsCallbackService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.CacheMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * 开启基于注解的RabbitMQ模式
 * * @EnableRabbit
 *
 * @author shuai.yang
 */
@EnableRabbit
@Configuration
public class RabbitConfiguration {

    /**
     * 自定义ackRabbitTemplate, 设置消息确认和回调
     */
    @Bean
    public CachingConnectionFactory sCachingConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        // 高速缓存中要维护的通道数。 默认情况下，按需分配通道（无限制），这表示最大高速缓存大小。
        // 要限制可用的频道，请参见{@link #setChannelCheckoutTimeout（long）}。@param sessionCacheSize通道缓存大小。@see #setChannelCheckoutTimeout（long）
        // 默认配置25
        connectionFactory.setChannelCacheSize(25);
        // CHANNEL->缓存单一通道, CONNECTION->缓存连接和每个连接中的通道, 默认CHANNEL
        connectionFactory.setCacheMode(CacheMode.CHANNEL);
        connectionFactory.setConnectionCacheSize(1);
        // 使用缓存模式CONNECTION时设置连接限制。当达到限制并且没有空闲连接时，{@ link＃setChannelCheckoutTimeout（long）channelCheckoutTimeLimit}用于等待连接变为空闲。
        // 默认Integer.MAX_VALUE
        connectionFactory.setConnectionLimit(Integer.MAX_VALUE);
        connectionFactory.setChannelCheckoutTimeout(0L);
        // 主机
        connectionFactory.setHost("192.168.41.125");
        // 端口
        connectionFactory.setPort(5672);
        // 用户名
        connectionFactory.setUsername("rabbitmq");
        // 密码
        connectionFactory.setPassword("rabbitmq");
        // 虚拟主机
        connectionFactory.setVirtualHost("/");
        // 通过URI方式设置连接, 主机，端口，用户名，密码和虚拟主机.当其中任一参数未设置则ConnectionFactory的对应变量保持不变
        // amqp://username:123456@192.168.1.131:5672
        /// connectionFactory.setUri("");
        // 集群地址, 当不为空时会覆盖host和port参数, "host[:port],..."
        connectionFactory.setAddresses("");
        connectionFactory.setRequestedHeartBeat(0);
        connectionFactory.setConnectionTimeout(0);
        connectionFactory.setCloseTimeout(0);
        connectionFactory.setBeanName("");
        // 开启returnCallback
        connectionFactory.setPublisherReturns(true);
        // 生产者消息确认confirmCallback:NONE->禁用,SIMPLE,CORRELATED
        connectionFactory.setPublisherConfirmType(ConfirmType.SIMPLE);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate sRabbitTemplate(CachingConnectionFactory sCachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(sCachingConnectionFactory);
        // producer->rabbitmq broker cluster->exchange->queue->consumer
        // message 从 producer 到 rabbitmq broker cluster 则会返回一个 confirmCallback 。
        // 消息只要被 rabbitmq broker 接收到就会执行 confirmCallback, 如果是 cluster 模式，需要所有 broker 接收到才会调用 confirmCallback。
        rabbitTemplate.setConfirmCallback(new RabbitConfirmCallbackService());
        // message 从 exchange->queue 投递失败则会返回一个 returnCallback
        rabbitTemplate.setReturnsCallback(new RabbitReturnsCallbackService());
        // 当mandatory标志位设置为true时，如果exchange根据自身类型和消息routeKey无法找到一个符合条件的queue，
        // 那么会调用basic.return方法将消息返回给生产者（Basic.Return + Content-Header + Content-Body）；
        // 当mandatory设置为false时，出现上述情形broker会直接将消息扔掉。
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }

    @Bean
    public SimpleMessageListenerContainer sMessageContainer(CachingConnectionFactory sCachingConnectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(sCachingConnectionFactory);
        // 监听队列, 支持多个
        container.setQueues();
        // 消费者数量
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        // 是否重回队列
        container.setDefaultRequeueRejected(false);
        // 是否自动ACK
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 预加载消息数, 默认250
        container.setPrefetchCount(250);
        // 设置监听外露
        container.setExposeListenerChannel(true);
        // 设置消费端标签策略
        container.setConsumerTagStrategy(queue -> queue + "_" + UUID.randomUUID().toString());
        // 设置消息监听
        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            // 手动ACK, 第二个参数表示是否开启批量确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            System.out.println(message);
        });
        return container;
    }
}
