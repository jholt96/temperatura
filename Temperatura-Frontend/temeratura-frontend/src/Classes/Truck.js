export default class Truck {

    hostname;
    env;
    currentTemperature = 0;
    currentHumidity = 0;
    temperatureCeilingThreshold = 0; 
    temperatureFloorThreshold= 0; 
    humidityCeilingThreshold = 0; 
    humidityFloorThreshold = 0; 
    timestamp = "";

    constructor(trucks) {
        this.hostname = trucks.hostname;
        this.env = trucks.env;
    }

}