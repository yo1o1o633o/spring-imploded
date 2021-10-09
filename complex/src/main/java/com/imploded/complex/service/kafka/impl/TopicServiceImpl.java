package com.imploded.complex.service.kafka.impl;

import com.imploded.complex.service.kafka.TopicService;
import kafka.admin.TopicCommand;

/**
 * @author shuai.yang
 */
public class TopicServiceImpl implements TopicService {

    @Override
    public void createTopic() {
        String[] options = new String[]{
                "--zookeeper",
                "localhost:2181/kafka",
                "--create",
                "--replication-factor", "1",
                "--partitions", "1",
                "--topic", "topic-create-api"
        };
        TopicCommand.main(options);
    }
}
