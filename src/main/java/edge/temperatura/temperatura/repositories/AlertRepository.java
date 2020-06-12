package edge.temperatura.temperatura.repositories;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import edge.temperatura.temperatura.models.Alerts;


public interface AlertRepository extends MongoRepository<Alerts, String>{

    Optional<Alerts> findBytimestamp(String timestamp);
}