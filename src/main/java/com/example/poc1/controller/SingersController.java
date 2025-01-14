package com.example.poc1.controller;

import com.example.poc1.entity.LogMessage;
import com.example.poc1.entity.Singers;
import com.example.poc1.kafka.KafkaProducerService;
import com.example.poc1.service.SingersService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/singers")
public class SingersController {
    @Autowired
    private SingersService singersService;
    @Autowired
    private KafkaProducerService kafkaProducerService;
    private static final Logger logger = LogManager.getLogger(SingersController.class);
    private static final String KAFKA_TOPIC = "singers-logs";

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Singers singers){
        ResponseEntity<String> responseEntity = null;
            Integer in = singersService.saveSingers(singers);
            LogMessage logMessage = new LogMessage(String.format("Singer %d created", in), "INFO");
            logger.info(logMessage.getMessage());
            kafkaProducerService.sendMessage(KAFKA_TOPIC, logMessage);
            responseEntity = new ResponseEntity<String>("Singer" + in + "created", HttpStatus.CREATED);
        return responseEntity;
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody Singers singers)
    {
        ResponseEntity<String> responseEntity = null;
        boolean available = singersService.isAvailable(singers.getSingerPosition());
        if(available) {
            singersService.updateSingers(singers);
            LogMessage logMessage = new LogMessage(String.format("Updated singer with ID: %d", singers.getSingerPosition()), "INFO");
            logger.info(logMessage.getMessage());
            kafkaProducerService.sendMessage(KAFKA_TOPIC, logMessage);
            responseEntity = new ResponseEntity<String>("Updated Successfully", HttpStatus.OK);
        }
        else {
            LogMessage logMessage = new LogMessage(String.format("Singer with ID: %d not found for update", singers.getSingerPosition()),"ERROR");
            logger.warn(logMessage.getMessage());
            kafkaProducerService.sendMessage(KAFKA_TOPIC, logMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Record " + singers.getSingerPosition() + " not found");
        }
        return responseEntity;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Integer id)
    {
        ResponseEntity<String> responseEntity = null;
        boolean isAvailable = singersService.isAvailable(id);
        if(isAvailable)
        {
            singersService.deleteSingers(id);
            LogMessage logMessage = new LogMessage(String.format("Deleted singer with ID: %d", id),"INFO");
            logger.info(logMessage.getMessage());
            kafkaProducerService.sendMessage(KAFKA_TOPIC, logMessage);
            responseEntity = new ResponseEntity<String>("Deleted Successfully", HttpStatus.OK);
        }
        else {
            LogMessage logMessage = new LogMessage(String.format("Singer with ID: %d not found for deletion", id), "ERROR");
            logger.warn(logMessage.getMessage());
            kafkaProducerService.sendMessage(KAFKA_TOPIC, logMessage);
            responseEntity = new ResponseEntity<String>("Singer "+ id + "not found", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Singers> getSingersById(@PathVariable Integer id){
        ResponseEntity<Singers> responseEntity = null;
        if(singersService.isAvailable(id)){
            Singers singer = singersService.getSingers(id);
            LogMessage logMessage = new LogMessage(String.format("Fetched singer with ID: %d", id),"INFO");
            logger.info(logMessage);
            kafkaProducerService.sendMessage(KAFKA_TOPIC, logMessage);
            responseEntity = new ResponseEntity<Singers>(singer, HttpStatus.OK);
        }
        else {
            LogMessage logMessage = new LogMessage(String.format("Singer with ID: %d not found", id), "WARN");
            logger.warn(logMessage.getMessage());
            kafkaProducerService.sendMessage(KAFKA_TOPIC, logMessage);
            responseEntity = new ResponseEntity<Singers>(new Singers(), HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @GetMapping(value = "/all", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Singers>> getAllSingers(){
        LogMessage logMessage = new LogMessage("Fetching all singers","TRACE");
        logger.info(logMessage.getMessage());
        kafkaProducerService.sendMessage(KAFKA_TOPIC, logMessage);
        ResponseEntity<List<Singers>> responseEntity = null;
        List<Singers> allSingers = singersService.getAllSingers();
        if(allSingers.isEmpty()) {
            LogMessage logMessage2 = new LogMessage("No singers found","ERROR");
            logger.warn(logMessage2.getMessage());
            kafkaProducerService.sendMessage(KAFKA_TOPIC, logMessage2);
            responseEntity = new ResponseEntity<List<Singers>>(new ArrayList<Singers>(), HttpStatus.NOT_FOUND);
        }
        else {
            LogMessage logMessage2 = new LogMessage(String.format("Fetched %d singers", allSingers.size()), "INFO");
            logger.info(logMessage2.getMessage());
            kafkaProducerService.sendMessage(KAFKA_TOPIC, logMessage2);
            responseEntity = new ResponseEntity<List<Singers>>(allSingers, HttpStatus.OK);
        }
        return responseEntity;
    }

}
