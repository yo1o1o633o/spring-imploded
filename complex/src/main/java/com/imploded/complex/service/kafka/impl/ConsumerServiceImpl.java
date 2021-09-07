package com.imploded.complex.service.kafka.impl;

import com.imploded.complex.service.kafka.ConsumerService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.*;

/**
 * @author shuai.yang
 */
public class ConsumerServiceImpl implements ConsumerService {

    private Properties properties;

    public ConsumerServiceImpl() {
        this.properties = initConfig();
    }

    @Override
    public void receiveMessage() {
        // 初始化配置
        Properties properties = initConfig();
        // 创建消费者, 非线程安全的
        KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(properties);
        // 订阅主题, 多个重载接口, 支持正则, 集合形式传入多个主题
        consumer.subscribe(Arrays.asList("topic_1", "topic_2"));
        // 多次订阅以最后一次的订阅为准
        consumer.subscribe(Arrays.asList("topic_3", "topic_4"));
        // 通过正则订阅主题, 使用此方式在后续有新建的主题符合这个正则时会自动被订阅消费
        consumer.subscribe(Collections.singletonList("topic-.*"));
        // 订阅指定主题的指定分区
        consumer.assign(Collections.singleton(new TopicPartition("topic-5", 1)));
        // 获取主题的分区信息列表
        List<PartitionInfo> partitionInfos = consumer.partitionsFor("topic-6");
        for (PartitionInfo partitionInfo : partitionInfos) {
            // 主题名
            String topic = partitionInfo.topic();
            // 分区号
            int partition = partitionInfo.partition();
            // 当前的leader结点
            Node leader = partitionInfo.leader();
            // 分区的AR集合
            Node[] replicas = partitionInfo.replicas();
            // 分区的ISR集合
            Node[] inSyncReplicas = partitionInfo.inSyncReplicas();
            // 分区的OSR集合
            Node[] offlineReplicas = partitionInfo.offlineReplicas();
        }
        // 拉取一批消息, 超时时间1秒. 如果当前应用线程专用消费, 则可以设置成最大值.
        ConsumerRecords<Object, Object> records = consumer.poll(Duration.ofSeconds(1));
        // 获取消息集中指定主题的消息
        records.records("topic-1");
        // 获取消息集中指定主题中指定分区消息
        records.records(new TopicPartition("topic-2", 1));
        // 获取消息集中的所有分区
        Set<TopicPartition> partitions = records.partitions();
        Iterator<ConsumerRecord<Object, Object>> iterator = records.iterator();
        while (iterator.hasNext()) {
            // 转成迭代器迭代每条消息
            ConsumerRecord<Object, Object> consumerRecord = iterator.next();
        }
        // 取消订阅, 如果订阅时指定的主题为空数组, 则也相当于取消订阅操作
        consumer.unsubscribe();
    }

    private Properties initConfig() {
        Properties properties = new Properties();
        // 连接Kafka集群的Broker地址列表
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9001");
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "");
        // 相对生产者特有的参数, 消费组名称. 不能为空
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group_1");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    private void getConsumer() {
        // 创建一个消费者
        KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(this.properties);
        // 订阅主题
        consumer.subscribe(Collections.singleton("topic-1"));
        // 拉取一批消息
        ConsumerRecords<Object, Object> records = consumer.poll(Duration.ofSeconds(Long.MAX_VALUE));
        // 获取拉取到的消息集中的所有分区
        Set<TopicPartition> partitions = records.partitions();
        // 遍历分区
        for (TopicPartition partition : partitions) {
            // 根据分区号在消息集中获取消息列表
            List<ConsumerRecord<Object, Object>> recordList = records.records(partition);
            // 同步提交
            consumer.commitSync();
        }
    }
}
