/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Service: Provides Business Logic for all Logic that changes the trucks collection. 
This includes the logic for the creation of trucks and alerts that happens in the KafkaConsumerService. 
this also includes logic for the userAccountsService around favorite trucks. 
This also includes the logic for the Rest Controller involved with trucks. 


Imporant Design Notes: 

The reason I did not split This Service into two seperate ones for Services and for Rest Controller is they both rely on the Map of Trucks. 
The reason for the Map of Trucks is this is meant to be a scalable solution for x number of trucks. 
If 1000 trucks are producing messages every 5 seconds, and each of those messages queries the database,
this can create a huge bottleneck in the system. The map gets rid of this problem by only query the data in the database when it needs to be changed. 


*/
package edge.temperatura.temperatura.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edge.temperatura.temperatura.models.Alerts;
import edge.temperatura.temperatura.models.Trucks;
import edge.temperatura.temperatura.payloads.KafkaMessage;
import edge.temperatura.temperatura.repositories.AlertRepository;
import edge.temperatura.temperatura.repositories.TruckRepository;

@Service
public class TrucksServiceImpl {

    @Autowired 
    private TruckRepository truckRepository;

    @Autowired
    private AlertRepository alertRepository;

    private Map<String,Trucks> trucks = new HashMap<>();

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Business Logic for Kafka Consumer
    public void createMapOfTrucks(){
        
        List<Trucks> listTrucks = truckRepository.findAll();

        for( int i = 0; i < listTrucks.size(); i++){
            trucks.put(listTrucks.get(i).getHostname(), listTrucks.get(i));
        }
    }

    public Map<String,Trucks> getMapOfTrucks(){
        return trucks;
    }

    public void addToMapOfTrucks(Trucks truck){

        trucks.put(truck.getHostname(), truck);
    }

    public Trucks getOneTruckFromMapOfTrucks(String hostname){
        return trucks.get(hostname);
    }

    //passes by reference and changes the message and truck
    public Trucks createAlert(KafkaMessage message, final Trucks truck){
        Trucks tempTruck = truck;
        Alerts newAlert = new Alerts(message.getTimestamp(), message.getTemperature(), message.getHumidity(), message.getTempThreshold());

        tempTruck.addAlert(newAlert.get_id());
        message.setAlert(true);

        alertRepository.save(newAlert);
        truckRepository.save(tempTruck);

        return tempTruck;
    }

    public Trucks createTruck(final KafkaMessage newMessage){

        Trucks truck = new Trucks(newMessage.getHostname(), newMessage.getEnv());
        truckRepository.save(truck);

        return truck;
    }
    public List<Trucks> getListofFavTrucks(List<String> truckIds){
        
        List<Trucks> favoriteTrucks = (List<Trucks>) truckRepository.findAllById(truckIds);

        return favoriteTrucks;
    }

////////////////////////////////////////////////////////////////////////////////////////
//Rest Controller methods

    public List<Trucks> getAllTrucks() {
        return truckRepository.findAll();
    }

    public Optional<Trucks> getById(String hostname) {
        return truckRepository.findByhostname(hostname);
    }
    
    public List<Alerts> getAlerts(String hostname) {
        
        Trucks truck = truckRepository.findByhostname(hostname)
                        .orElseThrow(() -> new RuntimeException("Truck does not exist!"));

        List<Alerts> arrList = new ArrayList<Alerts>();
        arrList = (List<Alerts>) alertRepository.findAllById(truck.getAlertsId());

        return arrList;
    }

    public void clearAlerts(String hostname){

        Trucks truck = truckRepository.findByhostname(hostname)
                                      .orElseThrow(() -> new RuntimeException("Truck Does Not Exist!"));

        Iterable<Alerts> itrAlerts= alertRepository.findAllById(truck.getAlertsId());

        itrAlerts.forEach(alert -> {
            alertRepository.delete(alert);
            });
            truck.clearAlerts();
            truckRepository.save(truck);

            trucks.put(truck.getHostname(), truck);

    }
    public void deleteTruck(String hostname){
        
        clearAlerts(hostname);
        Trucks truck = truckRepository.findByhostname(hostname)
                                      .orElseThrow(()-> new RuntimeException("Truck Does Not Exist!"));
        
        truckRepository.delete(truck);

    }
}