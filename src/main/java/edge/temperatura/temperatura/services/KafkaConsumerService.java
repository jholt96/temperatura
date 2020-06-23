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

Keeps a Hashmap of 'problem' trucks. if the truck drops below the threshold and it is in the hashmap then it will drop to zero. 
This could potentially be an issue if it is at the threshold and going back and forth but in the case of this application, it will be up to 
the application running on the truck to set a proper threshold that if it in this state the cargo will not be damaged. Another note on this topic, 
even though it may not send an alert the dashboard will still show the current state of the trucks for the business user to determine if it is important. 
TL/DR: Basically trucks need to set a threshold that cargo will not be endangered. 

*/
package edge.temperatura.temperatura.services;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;


import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import edge.temperatura.temperatura.models.Trucks;
import edge.temperatura.temperatura.payloads.KafkaMessage;

@Service
public class KafkaConsumerService implements ConsumerSeekAware{
    
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private TrucksServiceImpl trucksServiceImpl;


    //private Map<String,Trucks> trucks;
    private Map<String,Short> alertCount = new HashMap<>();
    private final short messageThreshold = 24;

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

            Trucks truck = trucksServiceImpl.getOneTruckFromMapOfTrucks(newMessage.getHostname());
            
            if (newMessage.getTemperature() >= newMessage.getTempThreshold()){

                //if its in the problem child map
                if(alertCount.containsKey(newMessage.getHostname())){
                    //if it has 10 messages where it exceeded the threshold
                    if(alertCount.get(newMessage.getHostname()) == messageThreshold){
                        //create an alert and send it
                        truck = trucksServiceImpl.createAlert(newMessage, truck);

                        // then delete it to restart the count
                        alertCount.put(newMessage.getHostname(), (short) 0);            

                    }else{
                        //keep it on the watch list and increment
                        alertCount.put(newMessage.getHostname(), (short) (alertCount.get(newMessage.getHostname()) + 1));
                    }
                }else{
                    //else it is below the threshold again so reset its number
                    alertCount.put(newMessage.getHostname(), (short) 0);
                }

            }else if(alertCount.containsKey(newMessage.getHostname())){
                    alertCount.put(newMessage.getHostname(), (short) 0);
            }
        }else{
            Trucks truck = trucksServiceImpl.createTruck(newMessage);

            trucksServiceImpl.addToMapOfTrucks(truck);
        }

        //now pass the kafka message on to the frontend
        
        template.convertAndSend("/topic/edge", newMessage.toJson());

    }
}