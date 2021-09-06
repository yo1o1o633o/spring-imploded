package com.imploded.complex.service.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shuai.yang
 */
public class CustomPartitioner implements Partitioner {
    private final AtomicInteger count = new AtomicInteger(0);

    /**
     * 重写partition方法, 根据入参自定义实现分区计算逻辑
     * */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        if (keyBytes == null) {
            return count.getAndIncrement() % partitions.size();
        } else {
            return Utils.toPositive(Utils.murmur2(keyBytes)) % partitions.size();
        }
    }

    /**
     * 关闭分区器时的一些操作
     * */
    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
