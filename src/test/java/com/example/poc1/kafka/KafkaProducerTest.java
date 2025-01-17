package com.example.poc1.kafka;

import com.example.poc1.entity.LogMessage;
import org.junit.jupiter.api.Test;


public class KafkaProducerTest {
     KafkaProducerService producer = new KafkaProducerService("localhost:9092");

     @Test
     void sendEventTest(){
          producer.sendMessage("testTopic", new LogMessage("Test message", "INFO"));

     }

     @Test
     void closeTest(){
          producer.close();
     }
}
