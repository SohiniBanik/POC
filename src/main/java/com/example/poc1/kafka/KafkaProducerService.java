package com.example.poc1.kafka;

import com.example.poc1.entity.LogMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, LogMessage> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, LogMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, LogMessage logMessage) {
        kafkaTemplate.send(topic, logMessage);
    }
}
