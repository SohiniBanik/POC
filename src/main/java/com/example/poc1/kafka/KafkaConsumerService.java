package com.example.poc1.kafka;

import com.example.poc1.entity.LogMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
@Service
public class KafkaConsumerService {
    private static final Logger logger = LogManager.getLogger(KafkaConsumerService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "singer-logs", groupId = "singer-group")
    public void listen(String message){
        LogMessage logMessage = null;
        try {
            logMessage = objectMapper.readValue(message, LogMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        logger.info("Consumed Message: {}", logMessage.getMessage());
        logger.debug("Received message with level {}: {}", logMessage.getLevel(), logMessage.getMessage());
    }

}