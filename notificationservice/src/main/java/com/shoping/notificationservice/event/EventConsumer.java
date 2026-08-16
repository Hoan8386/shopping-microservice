package com.shoping.notificationservice.event;

import org.apache.kafka.common.errors.RetriableException;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

// https://docs.spring.io/spring-kafka/reference/retrytopic.html tài liệu
@Component
@Slf4j

public class EventConsumer {
    
    @RetryableTopic(
        attempts = "4", // 3 topic retry + 1 topic DLQ
        backOff = @BackOff(delay = 1000, multiplier = 2) ,// thời gian delay giữa các lần retry
        autoCreateTopics = "True",// tự động tạo topic
        dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR, // cơ chế retry
        include = {RetriableException.class, RuntimeException.class} // chỉ quan sát những lối này thôi
    )
    @KafkaListener(topics = "test", containerFactory = "kafkaListenerContainerFactory") // cấu hình containerFactory từ trong common service KafkaConfig.java
        public void listen(String message) {
            log.info("Received message : " + message);
            throw new RuntimeException("error test");
    }
    
    @DltHandler
    void handleDltMessage(@Payload String message) {
        log.info("DLT received message : " + message);
    }
}
