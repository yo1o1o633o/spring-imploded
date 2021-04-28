package com.s.imploded.configuration;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.CacheMode;
import java.util.concurrent.Executor;
import org.springframework.amqp.rabbit.connection.AbstractConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import java.util.ArrayList;
import org.springframework.amqp.rabbit.connection.AbstractConnectionFactory.AddressShuffleMode;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.amqp.rabbit.connection.PublisherCallbackChannelFactory;
import java.net.URI;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.support.ConditionalExceptionLogger;
import java.util.concurrent.ThreadFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import com.rabbitmq.client.AddressResolver;
import com.rabbitmq.client.RecoveryListener;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 开启基于注解的RabbitMQ模式
 *  * @EnableRabbit
 *
 * @author shuai.yang
 * */
@EnableRabbit
@Configuration
public class RabbitmqConfiguration {

    /**
     * 自定义ackRabbitTemplate, 设置消息确认和回调
     */
    @Bean
    public RabbitTemplate ackRabbitTemplate() {
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
        connectionFactory.setUri("");
        // 集群地址, 当不为空时会覆盖host和port参数, "host[:port],..."
        connectionFactory.setAddresses("");
        connectionFactory.setRequestedHeartBeat(0);
        connectionFactory.setConnectionTimeout(0);
        connectionFactory.setCloseTimeout(0);
        connectionFactory.setBeanName("");
        // 设置回调
        connectionFactory.setPublisherReturns(true);
        // 生产者消息确认:NONE->禁用,SIMPLE,CORRELATED
        connectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public Queue asyncMessage() {
        return new Queue("y.queue.test");
    }

    @Bean
    public Queue asyncMessage2() {
        return new Queue("y.queue.test.2");
    }

    @Bean
    public Queue asyncMessage3() {
        return new Queue("y.queue.test.3");
    }
}
