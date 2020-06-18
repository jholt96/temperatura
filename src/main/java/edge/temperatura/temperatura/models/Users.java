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
}