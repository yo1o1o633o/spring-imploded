package com.s.imploded.service.rabbit;

/**
 * @author shuai.yang
 */
public interface RabbitService {

    void writeMessageTopic();

    void writeMessageDirect();

    void writeMessageFanout();

    void writeMessageHeaders();
}
