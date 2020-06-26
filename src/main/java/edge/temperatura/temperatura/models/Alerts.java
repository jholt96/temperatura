/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: This is the model for the Alerts Collection in Mongodb
Uses Lombok to autogenerate getters and setters

*/
package edge.temperatura.temperatura.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "alerts")
public class Alerts {
    
    @Id
    private ObjectId _id;
    private String timestamp; 
    private double temperature;
    private double humidity;
    private double rollingAvg; 
    private String thresholdType;

    public Alerts(){}

    public Alerts(String timestamp, double temperature, double humidity, double rollingAvg, String thresholdType){
        this._id = ObjectId.get();
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.humidity = humidity;
        this.rollingAvg = rollingAvg;
        this.thresholdType = thresholdType; 
    }
}