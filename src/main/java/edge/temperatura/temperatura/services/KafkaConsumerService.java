package edge.temperatura.temperatura.services;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.google.gson.Gson;

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
    private KafkaMessage newMessage;


    //Initialize the hashmap with the existing trucks in the db collection
    @PostConstruct
    public void init() {

        trucksServiceImpl.createMapOfTrucks();
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        assignments.keySet().forEach(tp -> callback.seekToEnd(tp.topic(), tp.partition()));
    }

    @KafkaListener(topics="edgetemp")
    public void consume(@Payload String message) {

        Gson convert = new Gson();

        newMessage = convert.fromJson(message, KafkaMessage.class);

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
                        //add the updated truck to the hashmap
                        //trucks.put(truck.getHostname(), truck);
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