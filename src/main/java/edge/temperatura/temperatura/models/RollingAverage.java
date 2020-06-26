package edge.temperatura.temperatura.models;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;

public class RollingAverage {

    private LinkedList<Float> tempAvg;
    private LinkedList<Float> humidityAvg;
    @Getter
    private float rollingTempAvg;
    @Getter
    private float rollingHumidityAvg;

    @Value("${defaultMessageThreshold}")
    private static float messageThreshold;

    public RollingAverage(){
        tempAvg = new LinkedList<>();
        humidityAvg = new LinkedList<>();
        rollingTempAvg = 0;
    }

    public float calcNewTempRollingAvg(float newTempVal){

        if(tempAvg.size() == 50){

            rollingTempAvg -= tempAvg.removeFirst();
        }
        tempAvg.add(newTempVal);

        rollingTempAvg = (rollingTempAvg + newTempVal)/tempAvg.size();

        return rollingTempAvg;
    }


    public float calcNewHumidityRollingAvg(float newHumidityVal){

        if(humidityAvg.size() == 50){

            rollingHumidityAvg -= humidityAvg.removeFirst();
        }
        humidityAvg.add(newHumidityVal);

        rollingHumidityAvg = (rollingHumidityAvg + newHumidityVal)/humidityAvg.size();

        return rollingHumidityAvg;
    }
    
}