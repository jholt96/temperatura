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


TODO can implement rate of change for precision. need to implement job to clean the map of trucks. 
*/
package edge.temperatura.temperatura.services;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import edge.temperatura.temperatura.pojos.AlertMessage;
import edge.temperatura.temperatura.pojos.KafkaMessage;

@Service
public class KafkaConsumerService implements ConsumerSeekAware{
    
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private TrucksServiceImpl trucksServiceImpl;

    private ScheduledThreadPoolExecutor executor;

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Value("${taskTimeForAlertCheckInMinutes}")
    private short taskTimeForAlertCheck;
    
    private boolean checkIfPastThreshold(float rollingAvg, float ceilingThreshold, float floorThreshold){
        return (floorThreshold >= rollingAvg || rollingAvg >= ceilingThreshold);
    }

    private void createTask(KafkaMessage newMessage ){
        try {
            executor.schedule(() -> {

                if(trucksServiceImpl.getMapOfTrucks().containsKey(newMessage.getHostname())) {

                    trucksServiceImpl.getMapOfTrucks().get(newMessage.getHostname()).setTaskRunning(false);

                    float rollingTemperatureAvg = trucksServiceImpl.getMapOfTrucks().get(newMessage.getHostname()).getRollingtemperatureAvg();
                    float rollingHumidityAvg = trucksServiceImpl.getMapOfTrucks().get(newMessage.getHostname()).getRollingHumidityAvg();
                    AlertMessage newAlertMessage = null;

                    boolean pastThreshold = checkIfPastThreshold(rollingHumidityAvg, newMessage.getHumidityCeilingThreshold(), newMessage.getHumidityFloorThreshold())
                                            || checkIfPastThreshold(rollingTemperatureAvg, newMessage.getTemperatureCeilingThreshold(), newMessage.getTemperatureFloorThreshold());

                    if (pastThreshold) {

                        newAlertMessage = trucksServiceImpl.createAlert(newMessage,rollingTemperatureAvg,rollingHumidityAvg);

                        trucksServiceImpl.getMapOfTrucks().get(newMessage.getHostname()).setOverThresholdsDuringLastPoll(true);

                        template.convertAndSend("/topic/edge",newAlertMessage.toJson());

                    }else{
                            trucksServiceImpl.getMapOfTrucks().get(newMessage.getHostname()).setOverThresholdsDuringLastPoll(false);
                    }                    
                }
                else{
                    logger.info("Truck does not exist in the map anymore!");
                }

            }, taskTimeForAlertCheck, TimeUnit.MINUTES);   

        } catch (Exception e) {

            logger.error("Could not create task", e);
        }

    }


    //Initialize the hashmap with the existing trucks in the db collection
    @PostConstruct
    public void init() {

        trucksServiceImpl.createMapOfTrucks();
        executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(5);
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {

        assignments.keySet().forEach(tp -> callback.seekToEnd(tp.topic(), tp.partition()));
    }

    @KafkaListener(topics="${kafkaTopic}")
    public void consume(@Payload KafkaMessage newMessage) {

        try {
            if(trucksServiceImpl.getMapOfTrucks().containsKey(newMessage.getHostname())){

                float newHumidityAvg = trucksServiceImpl.getMapOfTrucks().get(newMessage.getHostname())
                                                    .calcNewHumidityRollingAvg(newMessage.getHumidity());

                float newtemperatureAvg = trucksServiceImpl.getMapOfTrucks().get(newMessage.getHostname())
                                                        .calcNewtemperatureRollingAvg(newMessage.getTemperature());

                trucksServiceImpl.getMapOfTrucks().get(newMessage.getHostname()).setTimeStamp(newMessage.getTimestamp());
                
                if ((this.checkIfPastThreshold(newtemperatureAvg, newMessage.getTemperatureCeilingThreshold(),newMessage.getTemperatureFloorThreshold()) 
                || this.checkIfPastThreshold(newHumidityAvg, newMessage.getHumidityCeilingThreshold(), newMessage.getHumidityFloorThreshold())) 
                && !(trucksServiceImpl.getMapOfTrucks().get(newMessage.getHostname()).isTaskRunning())) {

                    this.createTask(newMessage);
                    trucksServiceImpl.getMapOfTrucks().get(newMessage.getHostname()).setTaskRunning(true);

                    if(!trucksServiceImpl.getMapOfTrucks().get(newMessage.getHostname()).wasOverThresholdsDuringLastPoll()) {
                            
                        AlertMessage alertMessage = trucksServiceImpl.createAlert(newMessage, newtemperatureAvg, newHumidityAvg);
                        template.convertAndSend("/topic/edge",alertMessage.toJson());
                    }
                }else{
                    trucksServiceImpl.getMapOfTrucks().get(newMessage.getHostname()).setOverThresholdsDuringLastPoll(false);
                }
            }else{
                    trucksServiceImpl.createTruck(newMessage);
            }

            //now pass the kafka message on to the Websocket
            template.convertAndSend("/topic/edge", newMessage.toJson());

        } catch (Exception e) {
            logger.error("unable to process message", e);
        }

    }
}