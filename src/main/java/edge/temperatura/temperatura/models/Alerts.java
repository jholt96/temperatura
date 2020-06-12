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
    private double tempThreshold; 

    public Alerts(){}

    public Alerts(String timestamp, double temperature, double humidity, double tempThreshold){
        this._id = ObjectId.get();
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.humidity = humidity;
        this.tempThreshold = tempThreshold;
    }
}