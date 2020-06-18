package edge.temperatura.temperatura.apicontroller;

import org.springframework.web.bind.annotation.RestController;

import edge.temperatura.temperatura.models.Alerts;
import edge.temperatura.temperatura.models.Trucks;
import edge.temperatura.temperatura.services.TrucksServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/trucks")
public class TrucksController {

    @Autowired
    TrucksServiceImpl trucksServiceImpl;

    @GetMapping(value = "/")
    //@PreAuthorize("hasRole('ADMIN')")
    public List<Trucks> getAllTrucks() {
        return trucksServiceImpl.getAllTrucks();
    }

    @GetMapping(value = "/{hostname}")
    public Optional<Trucks> getById(@PathVariable("hostname") String hostname) {

        return trucksServiceImpl.getById(hostname);
    }
    
    @GetMapping(value = "/{hostname}/alerts")
    public List<Alerts> getAlerts(@PathVariable("hostname") String hostname) {
        return trucksServiceImpl.getAlerts(hostname);
    }

    @DeleteMapping(value = "/{hostname}/alerts/clear")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void clearAlerts(@PathVariable("hostname") String hostname){
        trucksServiceImpl.clearAlerts(hostname);
    }
}