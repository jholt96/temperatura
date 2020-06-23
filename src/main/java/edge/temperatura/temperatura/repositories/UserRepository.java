/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: Provides a DAO object for the User collection in Mongodb. 
Extends the mongoRepository interface and adds option to query based on email and to check if the email exists.
*/
package edge.temperatura.temperatura.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edge.temperatura.temperatura.models.Users;

public interface UserRepository extends MongoRepository<Users, String> {
  
    Optional<Users> findByEmail(String email);
    Boolean existsByEmail(String email);
  }