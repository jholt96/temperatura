/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: Provide a POJO for messages consumed from kafka. 
The messages are deserialized in json and then turned into this object. 


*/
package edge.temperatura.temperatura.pojos;

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

    private float temperatureCeilingThreshold; 
    private float temperatureFloorThreshold; 
    private float humidityCeilingThreshold; 
    private float humidityFloorThreshold; 

    @Value("${defaultTempCeilingThreshold}")
    private static float defaulttemperatureCeilingThreshold = 80; 
    @Value("${defaultTempFloorThreshold}")
    private static float defaulttemperatureFloorThreshold = 0; 
    @Value("${defaultHumidityCeilingThreshold}")
    private static float defaultHumidityCeilingThreshold = 50; 
    @Value("${defaultHumidityFloorThreshold}")
    private static float defaultHumidityFloorThreshold = 0; 

    private boolean alert;
    
    public KafkaMessage(){
        this.temperature = 0;
        this.humidity = 0;
        this.timestamp = "";
        this.hostname = "";
        this.env = "";
        this.alert = false;
        this.temperatureCeilingThreshold = defaulttemperatureCeilingThreshold;
        this.temperatureFloorThreshold = defaulttemperatureFloorThreshold; 
        this.humidityCeilingThreshold = defaultHumidityCeilingThreshold; 
        this.humidityFloorThreshold = defaultHumidityFloorThreshold;
    }
    public KafkaMessage(float temperature, float humidity, String timestamp, String deviceName, String env, 
                        float temperatureCeilingThreshold, float temperatureFloorThreshold, float humidityCeilingThreshold, float humidityFloorThreshold) {

        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = timestamp;
        this.hostname = deviceName;
        this.env = env;
        this.alert = false;

        this.temperatureCeilingThreshold = temperatureCeilingThreshold;
        this.temperatureFloorThreshold = temperatureFloorThreshold; 
        this.humidityCeilingThreshold = humidityCeilingThreshold; 
        this.humidityFloorThreshold = humidityFloorThreshold;
    }


    public String toJson(){
        return "{\"message\":{\"timestamp\": \"" + timestamp + "\"," +
        "\"hostname\": \"" + hostname + "\"," +
        "\"temperature\":" + temperature + "," +
        "\"env\":" + env + ","+
        "\"humidity\": " + humidity + "," +
        "\"temperatureCeilingThreshold\": " + temperatureCeilingThreshold + "," +
        "\"temperatureFloorThreshold\":" + temperatureFloorThreshold + "," +
        "\"humidityCeilingThreshold\":" + humidityCeilingThreshold + "," +
        "\"humidityFloorThreshold\":" + humidityFloorThreshold + "," +
        "\"alert\":" + alert + "}}";
    }


}