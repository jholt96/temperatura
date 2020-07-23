package edge.temperatura.temperatura.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertMessage {

    private String timestamp; 
    private double temperature;
    private double humidity;
    private String hostname;
    private float rollingTemperatureAvg; 
    private float rollingHumidityAvg; 
    private Boolean alert = true;

    public AlertMessage(String timestamp, double temperature, double humidity, float rollingTemperatureAvg, float rollingHumidityAvg, String hostname){
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.humidity = humidity;
        this.hostname = hostname;
        this.rollingTemperatureAvg = rollingTemperatureAvg;
        this.rollingHumidityAvg = rollingHumidityAvg;
        this.alert = true;
    }


    public String toJson(){
        return "{\"alert\": {\"timestamp\": \"" + timestamp + "\"," +
        "\"hostname\": \"" + hostname + "\"," +
        "\"temperature\": " + temperature + "," +
        "\"humidity\": " + humidity + "," +
        "\"rollingTemperatureAvg\": " + rollingTemperatureAvg + "," +
        "\"alert\":" + alert + "," +
        "\"rollingHumidityAvg\":" + rollingHumidityAvg + "}}";
    }

    
}