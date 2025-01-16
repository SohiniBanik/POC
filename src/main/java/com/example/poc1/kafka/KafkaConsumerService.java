package com.example.poc1.kafka;

import com.example.poc1.entity.LogMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.example.poc1.serializer.LogMessageDeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Service
public class KafkaConsumerService {
    private static final Logger logger = LogManager.getLogger(KafkaConsumerService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaConsumer<String,String> kafkaConsumer;

    public KafkaConsumerService(@Value("${spring.kafka.bootstrap-servers}")String bootstrapServers, @Value("${spring.kafka.consumer.group-id}") String groupId) {
        //config
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LogMessageDeSerializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        //consumer client
        this.kafkaConsumer = new KafkaConsumer<>(props);
    }

    public void consume(String topic) {
        //subscribe to topics
        kafkaConsumer.subscribe(Collections.singletonList(topic));

        //polling for message
        try{
            while(true){
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
                for(ConsumerRecord<String, String> record : records){
                    processMessage(record.value());
                }
            }
        } catch (Exception e) {
            logger.error("Error while consuming messages", e);

        } finally {
            kafkaConsumer.close();
        }
    }

    private void processMessage(String message) {
        LogMessage logMessage;
        //process message 
        try{
            logMessage = objectMapper.readValue(message, LogMessage.class);
            logger.info("Consumed Message: {}", logMessage.getMessage());
            logger.debug("Received message with level {}: {}", logMessage.getLevel(), logMessage.getMessage());
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse message: {}", message, e);
        }

    }

}