package edge.temperatura.temperatura.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edge.temperatura.temperatura.models.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    
    Boolean existsByEmail(String email);
  }