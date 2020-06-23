/*
Author: Josh Holt
Temperatura Backend
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: 
This is the model for the User Collection in Mongodb.
This model holds the data around users. It has 2 manual dbrefs for roles and for trucks. 
the trucks dbref holds the users favorited truck's ids. 

the password is encrypted using bcrypt. 

Uses Lombok to autogenerate getters and setters

*/
package edge.temperatura.temperatura.models;

import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "user")
@Getter
@Setter
public class Users {

    @Id
    private ObjectId _id;
 
    private String email;
    private String username;
 
    private String password;

    private Set<String> favoriteTrucksIds;
    private Set<UserRole> roles;
  
    public Users(String username, String email, String password) {
      this._id = ObjectId.get();
      this.username = this.email = email;
      this.password = password;
      this.favoriteTrucksIds = new HashSet<>();
      this.roles = new HashSet<>();
    }

    public void addFavTruck(String id){
      this.favoriteTrucksIds.add(id);
    }
}