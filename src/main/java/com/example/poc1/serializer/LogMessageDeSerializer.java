package com.example.poc1.serializer;

import com.example.poc1.entity.LogMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class LogMessageDeSerializer implements Deserializer<LogMessage> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public LogMessage deserialize(String s, byte[] data) {
        try {
            return objectMapper.readValue(data, LogMessage.class);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing LogMessage", e);
        }
    }


}
