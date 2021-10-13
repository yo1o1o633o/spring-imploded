package com.imploded.complex.service.kafka;

import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * @author shuai.yang
 */
public interface ProducerService {
    /**
     * 发后即忘, 不做任何处理
     */
    void sendMessage();

    /**
     * 同步阻塞发送
     */
    void syncSendMessage();

    /**
     * 同步阻塞发送, 并返回元数据
     *
     * @return RecordMetadata
     */
    RecordMetadata syncSendMessageResult();

    /**
     * 同步阻塞发送, 可指定阻塞超时时间, 并返回元数据
     *
     * @return RecordMetadata
     */
    RecordMetadata syncSendMessageResultTimeOut();

    /**
     * 异步发送
     */
    void asyncSendMessage();
}
