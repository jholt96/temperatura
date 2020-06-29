/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: Provide a rest Controller for Users to interact with trucks. They must be authorized with roles of either ADMIN or VIEWER in order to use these apis. 

Apis: 
GET all Trucks
GET a single truck
GET alerts for one trucks
DELETE all alerts for one truck

*/
package edge.temperatura.temperatura.apicontroller;

import org.springframework.web.bind.annotation.RestController;

import edge.temperatura.temperatura.models.Alerts;
import edge.temperatura.temperatura.models.Trucks;
import edge.temperatura.temperatura.services.TrucksServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> clearAlerts(@PathVariable("hostname") String hostname){

        trucksServiceImpl.clearAlerts(hostname);

        return ResponseEntity.ok().body("Truck Deleted");
    }

    @DeleteMapping(value = "/{hostname}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTruck(@PathVariable("hostname") String hostname){
        
        trucksServiceImpl.deleteTruck(hostname);

        return ResponseEntity.ok().body("Truck Deleted");
    }
}