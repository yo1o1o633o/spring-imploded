package com.imploded.rabbit.service;

/**
 * @author shuai.yang
 */
public interface RabbitService {

    void writeMessageTopic();

    void writeMessageDirect();

    void writeMessageFanout();

    void writeMessageHeaders();
}
