/*
Author: Josh Holt
Temperatura Backend
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: 
This is the model for the Role Collection in Mongodb.
This model holds the various roles that a user can have. 

Uses Lombok to autogenerate getters and setters
*/
package edge.temperatura.temperatura.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;


@Document(value = "roles")
@Getter
@Setter
public class Role {
    
    @Id
    private ObjectId _id;

    private UserRole name;

    public Role() {
        this.name = UserRole.VIEWER;
        this._id = ObjectId.get();
    }

    public Role(UserRole name) {
        this.name = name;
        this._id = ObjectId.get();
      }
}