package edge.temperatura.temperatura.pojos;

import java.util.LinkedList;


import lombok.Getter;
import lombok.Setter;

public class RollingAverage {

    private LinkedList<Float> temperatureAvg;
    private LinkedList<Float> humidityAvg;
    @Getter
    private float rollingtemperatureAvg;
    @Getter
    private float rollingHumidityAvg;

    private double rollingtemperatureSum;
    private double rollingHumiditySum;

    @Getter
    @Setter
    private boolean taskRunning;

    @Setter
    private boolean overThresholdsDuringLastPoll;

    @Getter
    @Setter
    private String timeStamp;

    private int messageThreshold;

    public RollingAverage(int messageThreshold){
        temperatureAvg = new LinkedList<>();
        humidityAvg = new LinkedList<>();
        rollingtemperatureAvg = 0;
        rollingHumidityAvg = 0;
        rollingtemperatureSum = 0;
        rollingHumiditySum = 0;
        taskRunning = false;
        overThresholdsDuringLastPoll = false;
        this.messageThreshold = messageThreshold;
    }

    public float calcNewtemperatureRollingAvg(float newtemperatureVal){

        if(temperatureAvg.size() >= messageThreshold){

            rollingtemperatureSum -= temperatureAvg.removeFirst();
        }
        temperatureAvg.add(newtemperatureVal);
        rollingtemperatureSum += newtemperatureVal;

        rollingtemperatureAvg = (float)rollingtemperatureSum/temperatureAvg.size();

        return rollingtemperatureAvg;
    }


    public float calcNewHumidityRollingAvg(float newHumidityVal){

        if(humidityAvg.size() >= messageThreshold){

            rollingHumiditySum -= humidityAvg.removeFirst();
        }
        humidityAvg.add(newHumidityVal);
        rollingHumiditySum += newHumidityVal;

        rollingHumidityAvg = (float) rollingHumiditySum/humidityAvg.size();

        return rollingHumidityAvg;
    }

    public boolean wasOverThresholdsDuringLastPoll(){
        return this.overThresholdsDuringLastPoll;
    }
    
}