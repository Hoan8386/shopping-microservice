package com.shoping.commonservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

// cấu hình để gửi message 
@Service
@Slf4j
public class KafkaService {
        @Autowired
        private KafkaTemplate<String,String> kafkaTemplate;

        public void sendMessage (String topic , String message) {
            kafkaTemplate.send(topic,message);
            log.info("message send to topic "+topic);
        }
}
