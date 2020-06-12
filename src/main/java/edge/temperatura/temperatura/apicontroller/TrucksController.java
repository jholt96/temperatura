package edge.temperatura.temperatura.apicontroller;

import org.springframework.web.bind.annotation.RestController;

import edge.temperatura.temperatura.models.Alerts;
import edge.temperatura.temperatura.models.Trucks;
import edge.temperatura.temperatura.repositories.AlertRepository;
import edge.temperatura.temperatura.repositories.TruckRepository;
import edge.temperatura.temperatura.services.KafkaConsumerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/trucks")
public class TrucksController {

    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    KafkaConsumerService kafkaConsumerService;

    private List<Alerts> arrList;

    @GetMapping(value = "/")
    public List<Trucks> getAllTrucks() {
        return truckRepository.findAll();
    }

    @GetMapping(value = "/{hostname}")
    public Optional<Trucks> getById(@PathVariable("hostname") String hostname) {
        return truckRepository.findByhostname(hostname);
    }
    
    @GetMapping(value = "/{hostname}/alerts")
    public List<Alerts> getAlerts(@PathVariable("hostname") String hostname) {
        Optional<Trucks> truck = truckRepository.findByhostname(hostname);

        this.arrList = new ArrayList<Alerts>();

		truck.ifPresent(t -> {
            this.arrList = (List<Alerts>) alertRepository.findAllById(t.getIterableAlertsIds());
        });

        return this.arrList;
    }

    @DeleteMapping(value = "/{hostname}/alerts/clear")
    public List<Alerts> clearAlerts(@PathVariable("hostname") String hostname){

        Optional<Trucks> truck = truckRepository.findByhostname(hostname);

		truck.ifPresent(t -> {
            Iterable<Alerts> itrAlerts= alertRepository.findAllById(t.getIterableAlertsIds());

            itrAlerts.forEach(alert -> {
                alertRepository.delete(alert);
            });
            t.clearAlerts();
            truckRepository.save(t);

            kafkaConsumerService.resetTrucksMap(t);
        });

        return arrList;
    }
}