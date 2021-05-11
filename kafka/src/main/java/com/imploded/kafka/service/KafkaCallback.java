package com.imploded.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author shuai.yang
 */
@Slf4j
public class KafkaCallback implements ListenableFutureCallback<SendResult<String, Object>> {
    @Override
    public void onFailure(Throwable throwable) {
        log.info("异常回调: {}", throwable.getMessage());
    }

    @Override
    public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
        log.info("成功回调: {}", stringObjectSendResult);
    }
}
