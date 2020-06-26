/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Service: Creates a Kafka Consumer Service that consumes a message from kafka,
checks which truck the data belongs to, adds it to the database if it is a new truck, and then checks if the data is above or below the threshold. 
If it sustains a temperature reading above the threshold for 2 minutes then it will create an alert and send it to the frontend. 
Otherwise it will just pass the message into the frontend. 

Important Design Choices: 

Since this application is meant to show real time data into the trucks, in the event of a restart of this app, 
it will seek to the end of the topic offset and ignore the rest. 

Keeps a Hashmap of 'problem' trucks. 


TODO can implement rolling average and rate of change for precision
*/
package edge.temperatura.temperatura.services;

import java.util.Map;

import javax.annotation.PostConstruct;


import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import edge.temperatura.temperatura.payloads.KafkaMessage;

@Service
public class KafkaConsumerService implements ConsumerSeekAware{
    
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private TrucksServiceImpl trucksServiceImpl;

    @Value("${defaultMessageThreshold}")
    private static short messageThreshold;
    
    private boolean checkIfPastThreshold(float newHumidityAvg, float ceilingThreshold, float floorThreshold){
        return !(floorThreshold <= newHumidityAvg && newHumidityAvg <= ceilingThreshold);
    }

    private void checkForAlert(KafkaMessage newMessage, String threshholdType, float rollingAvg){


    }


    //Initialize the hashmap with the existing trucks in the db collection
    @PostConstruct
    public void init() {

        trucksServiceImpl.createMapOfTrucks();
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        assignments.keySet().forEach(tp -> callback.seekToEnd(tp.topic(), tp.partition()));
    }

    @KafkaListener(topics="${kafkaTopic}")
    public void consume(@Payload KafkaMessage newMessage) {

        //if the truck exists then test if it is past its threshold
        if(trucksServiceImpl.getMapOfTrucks().containsKey(newMessage.getHostname())){

            float newTempAvg = trucksServiceImpl.getMapOfTrucks().get(newMessage.getHostname())
                             .calcNewHumidityRollingAvg(newMessage.getHumidity());

            float newHumidityAvg = trucksServiceImpl.getMapOfTrucks().get(newMessage.getHostname())
                             .calcNewTempRollingAvg(newMessage.getTemperature());
            
            //every 5 minutes check if the rolling average is passed the thresholds set by driver. possible solution is to use ScheduledExecutorService 
            if (this.checkIfPastThreshold(newTempAvg, newMessage.getTempCeilingThreshold(),newMessage.getTempFloorThreshold())){

                this.checkForAlert(newMessage, "temperature", newTempAvg);

            }else if(this.checkIfPastThreshold(newHumidityAvg, newMessage.getHumidityCeilingThreshold(), newMessage.getHumidityFloorThreshold())){

                this.checkForAlert(newMessage, "humidity", newHumidityAvg);

            }
        }else{
                trucksServiceImpl.createTruck(newMessage);
        }

        //now pass the kafka message on to the Websocket
        
        template.convertAndSend("/topic/edge", newMessage.toJson());
    }
}