/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: Provides a DAO object for the Alerts collection in Mongodb. 
Extends the mongoRepository interface and adds option to query based on timestamp.  
*/
package edge.temperatura.temperatura.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import edge.temperatura.temperatura.models.Alerts;


public interface AlertRepository extends MongoRepository<Alerts, String>{

    Optional<Alerts> findBytimestamp(String timestamp);

    List<Alerts> findBytruckId(ObjectId truckId);
}