package com.imploded.complex.service.kafka.impl;

import com.imploded.complex.service.kafka.ConfigService;
import com.imploded.complex.service.kafka.common.CustomPartitioner;
import com.imploded.complex.service.kafka.common.CustomProducerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * @author shuai.yang
 */
@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {

    @Override
    public Properties initProducerConfig() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "");
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "");
        // 重试次数, 对于可重试异常, 在此配置重复次数内自行恢复就不会抛出异常, 默认0
        properties.put(ProducerConfig.RETRIES_CONFIG, 10);
        // 默认序列化器, 可以自定义, 生产者和消费者的序列化器要保持一致
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 自定义分区器
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());
        // 自定义拦截器
        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, CustomProducerInterceptor.class.getName());
        return properties;
    }

    @Override
    public Properties initConsumerConfig() {
        Properties properties = new Properties();
        // 连接Kafka集群的Broker地址列表
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9001");
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "");
        // 相对生产者特有的参数, 消费组名称. 不能为空
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group_1");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        return properties;
    }
}
