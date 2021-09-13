package com.imploded.complex.service.kafka.common;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义消费者拦截器
 * @author shuai.yang
 * */
public class CustomConsumerInterceptor implements ConsumerInterceptor<String, String> {
    private static final long EXPIRE_INTERVAL = 10000;

    /**
     * 此方法在poll方法返回之前执行
     * 此方法抛出异常会被捕获, 不会向上传递异常
     * */
    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> records) {
        // 当前时间
        long now = System.currentTimeMillis();
        // 新的消息Map
        Map<TopicPartition, List<ConsumerRecord<String, String>>> newRecords = new HashMap<>();
        for (TopicPartition partition : records.partitions()) {
            List<ConsumerRecord<String, String>> recordList = records.records(partition);
            List<ConsumerRecord<String, String>> newRecordList = new ArrayList<>();
            // 取出每条消息判断时间
            for (ConsumerRecord<String, String> record : recordList) {
                // 时间在10秒内, 放入新数组
                if (now - record.timestamp() < EXPIRE_INTERVAL) {
                    newRecordList.add(record);
                }
            }
            // 组装新的消息Map
            if (!newRecordList.isEmpty()) {
                newRecords.put(partition, newRecordList);
            }
        }
        // 返回新的消息Map
        return new ConsumerRecords<>(newRecords);
    }

    @Override
    public void close() {

    }

    /**
     * 此方法在提交完消费位移后执行
     * */
    @Override
    public void onCommit(Map offsets) {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
