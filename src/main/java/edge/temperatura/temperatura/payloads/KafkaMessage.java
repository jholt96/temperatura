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
    private float temperature;
    private float humidity;
    private String hostname;
    private String env;

    private float tempCeilingThreshold; 
    private float tempFloorThreshold; 
    private float humidityCeilingThreshold; 
    private float humidityFloorThreshold; 

    @Value("${defaultTempCeilingThreshold}")
    private static float defaultTempCeilingThreshold; 
    @Value("${defaultTempFloorThreshold}")
    private static float defaultTempFloorThreshold; 
    @Value("${defaultHumidityCeilingThreshold}")
    private static float defaultHumidityCeilingThreshold; 
    @Value("${defaultHumidityFloorThreshold}")
    private static float defaultHumidityFloorThreshold; 

    private boolean alert;
    
    public KafkaMessage(){
        this.temperature = 0;
        this.humidity = 0;
        this.timestamp = "";
        this.hostname = "";
        this.env = "";
        this.alert = false;
    }
    public KafkaMessage(float temp, float humidity, String timestamp, String deviceName, String env, 
                        float tempCeilingThreshold, float tempFloorThreshold, float humidityCeilingThreshold, float humidityFloorThreshold) {

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