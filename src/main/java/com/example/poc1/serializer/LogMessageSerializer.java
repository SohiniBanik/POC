package com.example.poc1.serializer;

import com.example.poc1.entity.LogMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class LogMessageSerializer implements Serializer<LogMessage> {
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public byte[] serialize(String topic, LogMessage logMessage) {
        try {
            return objectMapper.writeValueAsBytes(logMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing LogMessage", e);
        }
    }

}
