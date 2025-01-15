//package com.example.poc1.kafka;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
//import org.springframework.kafka.listener.ContainerProperties;
//import org.springframework.kafka.listener.MessageListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class KafkaConsumerRunner implements ApplicationRunner {
//    private final ConsumerFactory<String, String> consumerFactory;
//    private static final Logger logger = LogManager.getLogger(KafkaProducerService.class);
//    @Autowired
//    public KafkaConsumerRunner(ConsumerFactory<String, String> consumerFactory) {
//        this.consumerFactory = consumerFactory;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        ContainerProperties containerProperties = new ContainerProperties("singer-logs");
//        containerProperties.setMessageListener(new MessageListener<String, String>() {
//
//            @Override
//            public void onMessage(ConsumerRecord<String, String> data) {
//                logger.info("Received message: {}", data.value());
//            }
//        });
//        containerProperties.setAckMode(ContainerProperties.AckMode.MANUAL);
//        ConcurrentMessageListenerContainer<String, String> container = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
//        container.start();
//    }
//}
