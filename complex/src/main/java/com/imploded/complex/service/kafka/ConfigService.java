package com.imploded.complex.service.kafka;

import java.util.Properties;

/**
 * @author shuai.yang
 */
public interface ConfigService {

    /**
     * 生产者配置参数
     * @return Properties
     */
    Properties initProducerConfig();

    /**
     * 消费者配置参数
     * @return Properties
     */
    Properties initConsumerConfig();
}
