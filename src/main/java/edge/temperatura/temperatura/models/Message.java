package edge.temperatura.temperatura.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class Message {
    
    private String timestamp;

    private double temperature;

    private double humidity;

    private String hostname;

    private String env;
    private double tempThreshold; 
    
    @Autowired
    public Message(){
        this.temperature = 0;
        this.tempThreshold = 82;
        this.humidity = 0;
        this.timestamp = "";
        this.hostname = "";
        this.env = "";
    }
    public Message(double temp, double humidity, String timestamp, String deviceName, String env, double tempThreshold){
        this.temperature = temp;
        this.humidity = humidity;
        this.timestamp = timestamp;
        this.hostname = deviceName;
        this.env = env;
        this.tempThreshold = tempThreshold;
    }


    public String toJson(){
        return "{\"timestamp\": \"" + timestamp + "\"," +
        "\"hostname\": \"" + hostname + "\"," +
        "\"temperature\": \"" + temperature + "\"," +
        "\"env\": \"" + env + "\","+
        "\"humidity\": \" " + humidity + "\"}";
    }


}