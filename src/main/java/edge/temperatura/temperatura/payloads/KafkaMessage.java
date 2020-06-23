/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: Provide a POJO for messages consumed from kafka. 
The messages are deserialized in json and then turned into this object. 


*/
package edge.temperatura.temperatura.payloads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaMessage {
    
    private String timestamp;
    private double temperature;
    private double humidity;
    private String hostname;
    private String env;
    private double tempThreshold; 
    private boolean alert;
    
    public KafkaMessage(){
        this.temperature = 0;
        this.tempThreshold = 82;
        this.humidity = 0;
        this.timestamp = "";
        this.hostname = "";
        this.env = "";
        this.alert = false;
    }
    public KafkaMessage(double temp, double humidity, String timestamp, String deviceName, String env, double tempThreshold){
        this.temperature = temp;
        this.humidity = humidity;
        this.timestamp = timestamp;
        this.hostname = deviceName;
        this.env = env;
        this.tempThreshold = tempThreshold;
        this.alert = false;
    }


    public String toJson(){
        return "{\"timestamp\": \"" + timestamp + "\"," +
        "\"hostname\": \"" + hostname + "\"," +
        "\"temperature\": \"" + temperature + "\"," +
        "\"env\": \"" + env + "\","+
        "\"humidity\": \" " + humidity + "\"," +
        "\"tempThreshold\": \" " + tempThreshold + "\"," +
        "\"alert\": \" " + alert + "\"}";
    }


}