package edge.temperatura.temperatura.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edge.temperatura.temperatura.models.Trucks;

public interface TruckRepository extends MongoRepository<Trucks, String> {

    Optional<Trucks> findByhostname(String hostname);
}