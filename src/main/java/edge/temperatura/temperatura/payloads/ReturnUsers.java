/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: Provide a POJO for the JWT response. 
When user logs in this object will be returned if they are successfully authenticated. 
*/
package edge.temperatura.temperatura.payloads;

import java.util.Set;

import org.bson.types.ObjectId;

import edge.temperatura.temperatura.models.UserRole;
import edge.temperatura.temperatura.models.Users;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnUsers {

    private ObjectId _id;
    private String email;
    private Set<String> favoriteTrucksIds;
    private Set<UserRole> roles;

    public void setUser(Users user){
        this._id = user.get_id();
        this.email = user.getEmail();
        this.favoriteTrucksIds = user.getFavoriteTrucksIds();
        this.roles = user.getRoles();
    }
}