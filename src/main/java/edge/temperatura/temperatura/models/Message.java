package edge.temperatura.temperatura.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
public class Message {
    
    @Getter
    @Setter
    private String timestamp;
    @Getter
    @Setter
    private double temperature;
    @Getter
    @Setter
    private double humidity;
    @Getter
    @Setter
    private String hostname;
    @Getter
    @Setter
    private String env;
    
    @Autowired
    public Message(){
        this.temperature = 0;
        this.humidity = 0;
        this.timestamp = "";
        this.hostname = "";
        this.env = "";
    }
    public Message(double temp, double humidity, String time, String deviceName, String env){
        this.temperature = temp;
        this.humidity = humidity;
        this.timestamp = time;
        this.hostname = deviceName;
        this.env = env;
    }


    public String toJson(){
        return "{\"timestamp\": \"" + timestamp + "\", \""
               + "\"hostname\": \"" + hostname + "\", \""
               + "\"temperature\": \"" + temperature + "\", \""
               + "\"env\": \"" + env + "\", \""
               + "\"humidity\": \"" + humidity + "\"}";
    }


}