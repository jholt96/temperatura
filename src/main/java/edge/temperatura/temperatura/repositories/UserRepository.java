package edge.temperatura.temperatura.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edge.temperatura.temperatura.models.Users;

public interface UserRepository extends MongoRepository<Users, String> {
  
    Optional<Users> findByEmail(String email);
    Boolean existsByEmail(String email);
  }