package com.s.imploded.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shuai.yang
 */
@Configuration
public class RabbitHeadersConfiguration {
    /**
     * 创建扇形交换机
     * 名字: direct.1
     */
    @Bean
    public HeadersExchange sHeadersExchange() {
        return new HeadersExchange("s.exchange.headers");
    }

    /**
     * 创建队列
     * 名字: s.queue.test
     * 持久化: true
     */
    @Bean
    public Queue sHeadersQueue() {
        return new Queue("s.queue.headers.1", true);
    }

    /**
     * 此处在绑定时设置条件, 写入消息时也会往头部Headers写入自定义键值对, 写入消息的headers和此处的进行匹配.
     * whereAll 完全匹配
     * whereAny 匹配其中一个键
     * */
    @Bean
    public Binding sHeadersBinding() {
        Map<String, Object> map = new HashMap<>();
        map.put("header1", 1);
        map.put("header2", 3);
        return BindingBuilder.bind(sHeadersQueue()).to(sHeadersExchange()).whereAll(map).match();
    }
}
