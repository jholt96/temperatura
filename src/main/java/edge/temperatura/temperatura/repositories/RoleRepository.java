/*
Author: Josh Holt
Temperatura Backend
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: Provides a DAO object for the Alerts collection in Mongodb. 
Extends the mongoRepository interface and adds option to query based on enum name of the role.
 */
package edge.temperatura.temperatura.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edge.temperatura.temperatura.models.Role;
import edge.temperatura.temperatura.models.UserRole;

public interface RoleRepository extends MongoRepository<Role,String>{

    Optional<Role> findByname(UserRole role);
}