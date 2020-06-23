/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: Provides a DAO object for the trucks collection in Mongodb. 
Extends the mongoRepository interface and adds option to query based on hostname of the truck. 
*/
package edge.temperatura.temperatura.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edge.temperatura.temperatura.models.Trucks;

public interface TruckRepository extends MongoRepository<Trucks, String> {

    Optional<Trucks> findByhostname(String hostname);
}