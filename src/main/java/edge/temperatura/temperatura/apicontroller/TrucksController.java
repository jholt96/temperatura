package edge.temperatura.temperatura.apicontroller;

import org.springframework.web.bind.annotation.RestController;

import edge.temperatura.temperatura.models.Trucks;
import edge.temperatura.temperatura.repositories.TruckRepository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/trucks")
public class TrucksController {

    @Autowired
    private TruckRepository truckRepository;

    @GetMapping(value = "/")
    public List<Trucks> getAllTrucks() {
        return truckRepository.findAll();
    }

    @GetMapping(value = "/{hostname}")
    public Optional<Trucks> getById(@PathVariable("hostname") String hostname) {
        return truckRepository.findByhostname(hostname);
    }
    @PostMapping(value = "/")
    public Trucks setById(@RequestBody Trucks truck) {
        truck.set_id(ObjectId.get());
        return truckRepository.save(truck);
    }    
    
    
}