package edge.temperatura.temperatura.models;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "user")
public class Users {

    @Id
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter    
    private String email;

    @Getter
    @Setter   
    private String password;
    
    @Getter
    @Setter
    private Set<Trucks> trucks;

    public Users() {
    }
  
    public Users(String username, String email, String password) {
      this.email = email;
      this.password = password;
      this.trucks = new HashSet<>();
    }
  

    
}