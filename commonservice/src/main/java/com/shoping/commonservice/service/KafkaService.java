package com.shoping.commonservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

// cấu hình để gửi message 
// Service khác
//    |
//    | KafkaService.sendMessage("emailTemplate", email)
//    v
// Kafka topic: emailTemplate
//    |
//    v
// EventConsumer.emailTemplate()
//    |
//    | EmailService.sendEmailWithTemplate(...)
//    v
// SMTP/Gmail gửi email

@Service
@Slf4j
public class KafkaService {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage(String topic, Object data) {
        String dataJson = objectMapper.writeValueAsString(data);
        kafkaTemplate.send(topic, dataJson);
        log.info("Message sent to topic {}", topic);
    }
}
