export default class Truck {

    hostname;
    env;
    currentTemperature = 0;
    currentHumidity = 0;
    temperatureCeilingThreshold = 0; 
    temperatureFloorThreshold= 0; 
    humidityCeilingThreshold = 0; 
    humidityFloorThreshold = 0; 
    timestamp = 0;

    constructor(trucks) {
        this.hostname = trucks.hostname;
        this.env = trucks.env;
    }

}