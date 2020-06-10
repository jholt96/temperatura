package edge.temperatura.temperatura.services;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import edge.temperatura.temperatura.models.Message;

@Service
public class KafkaConsumerService{
    
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private Message newMessage;


    @KafkaListener(topics="edgetemp")
    public void consume(@Payload String message) {

        Gson convert = new Gson();
        newMessage = convert.fromJson(message, Message.class);

        System.out.print(newMessage.toJson());

        

        //now have a Message object. 
        
        template.convertAndSend("/topic/edge", message);
    }
}