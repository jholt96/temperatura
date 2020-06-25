/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: Provide a POJO for messages consumed from kafka. 
The messages are deserialized in json and then turned into this object. 


*/
package edge.temperatura.temperatura.payloads;

import org.springframework.beans.factory.annotation.Value;

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

    private double tempCeilingThreshold; 
    private double tempFloorThreshold; 
    private double humidityCeilingThreshold; 
    private double humidityFloorThreshold; 

    @Value("${defaultTempCeilingThreshold}")
    private static double defaultTempCeilingThreshold; 
    @Value("${defaultTempFloorThreshold}")
    private static double defaultTempFloorThreshold; 
    @Value("${defaultHumidityCeilingThreshold}")
    private static double defaultHumidityCeilingThreshold; 
    @Value("${defaultHumidityFloorThreshold}")
    private static double defaultHumidityFloorThreshold; 

    private boolean alert;
    
    public KafkaMessage(){
        this.temperature = 0;
        this.humidity = 0;
        this.timestamp = "";
        this.hostname = "";
        this.env = "";
        this.alert = false;
    }
    public KafkaMessage(double temp, double humidity, String timestamp, String deviceName, String env, 
                        double tempCeilingThreshold, double tempFloorThreshold, double humidityCeilingThreshold, double humidityFloorThreshold) {

        this.temperature = temp;
        this.humidity = humidity;
        this.timestamp = timestamp;
        this.hostname = deviceName;
        this.env = env;
        this.alert = false;

        this.tempCeilingThreshold = tempCeilingThreshold;
        this.tempFloorThreshold = tempFloorThreshold; 
        this.humidityCeilingThreshold = humidityCeilingThreshold; 
        this.humidityFloorThreshold = humidityFloorThreshold;
    }


    public String toJson(){
        return "{\"timestamp\": \"" + timestamp + "\"," +
        "\"hostname\": \"" + hostname + "\"," +
        "\"temperature\": \"" + temperature + "\"," +
        "\"env\": \"" + env + "\","+
        "\"humidity\": \" " + humidity + "\"," +
        "\"tempCeilingThreshold\": \" " + tempCeilingThreshold + "\"," +
        "\"tempFloorThreshold\": \" " + tempFloorThreshold + "\"," +
        "\"humidityCeilingThreshold\": \" " + humidityCeilingThreshold + "\"," +
        "\"humidityFloorThreshold\": \" " + humidityFloorThreshold + "\"," +
        "\"alert\": \" " + alert + "\"}";
    }


}