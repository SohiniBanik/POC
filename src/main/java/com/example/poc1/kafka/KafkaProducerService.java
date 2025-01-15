package com.example.poc1.kafka;

import com.example.poc1.entity.LogMessage;
import org.apache.kafka.clients.producer.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.kafka.common.serialization.StringSerializer;
import  com.example.poc1.serializer.LogMessageSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class KafkaProducerService {

    private final KafkaProducer<String, LogMessage> kafkaProducer;
    private static final Logger logger = LogManager.getLogger(KafkaProducerService.class);

    public KafkaProducerService(@Value("${spring.kafka.bootstrap-servers}")String bootstrapServers) {
        //config
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LogMessageSerializer.class);
        this.kafkaProducer = new KafkaProducer<>(props);// producer interface
    }


    public void sendMessage(String topic, LogMessage logMessage) {
        ProducerRecord<String, LogMessage> record = new ProducerRecord<>(topic, logMessage); // record
        //send record
        kafkaProducer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e != null) {
                    logger.error("Error sending message to topic {}: {}", recordMetadata.topic(), e.getMessage(), e);
                }
                else{
                    logger.info("Message sent to topic {} partition {} with offset {}",
                            recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
                }
            }
        });
    }

    public void close() {
        // flush/close
        kafkaProducer.close();
    }

}
