package edge.temperatura.temperatura.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import edge.temperatura.temperatura.models.Role;
import edge.temperatura.temperatura.models.UserRole;

public interface RoleRepository extends MongoRepository<Role,String>{

    Optional<Role> findByname(UserRole role);
}