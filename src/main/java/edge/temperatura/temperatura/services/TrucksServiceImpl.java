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
        arrList = (List<Alerts>) alertRepository.findAllById(truck.getIterableAlertsIds());

        return arrList;
    }


    public void clearAlerts(String hostname){

        Optional<Trucks> truck = truckRepository.findByhostname(hostname);

		truck.ifPresent(noAlertsTruck -> {
            Iterable<Alerts> itrAlerts= alertRepository.findAllById(noAlertsTruck.getIterableAlertsIds());

            itrAlerts.forEach(alert -> {
                alertRepository.delete(alert);
            });
            noAlertsTruck.clearAlerts();
            truckRepository.save(noAlertsTruck);

            trucks.put(noAlertsTruck.getHostname(), noAlertsTruck);
        });

    }





}