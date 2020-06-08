package ibm.edge.edgekafkademo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Message {
    
    private String timestamp;
    private double temperature;
    private double humidity;
    private String hostname;
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

    public String getTime(){return timestamp; }
    public String getDeviceName(){return hostname;}
    public double getTemp(){return temperature;}
    public double getHumidity(){return humidity;}
    public String getEnv(){return env;}

    public void setTime(String newTime){this.timestamp = newTime; }
    public void setDeviceName(String newName){this.hostname = newName;}
    public void setTemp(double newTemp){this.temperature = newTemp;}
    public void setHumidity(double newHumidity){this.humidity = newHumidity;}
    public void setEnv(String newEnv){this.env = newEnv;}

    public String toJson(){
        return "{\"timestamp\": \"" + timestamp + "\", \""
               + "\"hostname\": \"" + hostname + "\", \""
               + "\"temperature\": \"" + temperature + "\", \""
               + "\"env\": \"" + env + "\", \""
               + "\"humidity\": \"" + humidity + "\"}";
    }


}