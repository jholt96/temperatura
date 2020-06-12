package edge.temperatura.temperatura.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    
    private String timestamp;
    private double temperature;
    private double humidity;
    private String hostname;
    private String env;
    private double tempThreshold; 
    private boolean alert;
    
    public Message(){
        this.temperature = 0;
        this.tempThreshold = 82;
        this.humidity = 0;
        this.timestamp = "";
        this.hostname = "";
        this.env = "";
        this.alert = false;
    }
    public Message(double temp, double humidity, String timestamp, String deviceName, String env, double tempThreshold){
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