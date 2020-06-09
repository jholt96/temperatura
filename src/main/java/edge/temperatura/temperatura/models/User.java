package edge.temperatura.temperatura.models;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "user")
public class User {

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
    @DBRef
    private Set<Trucks> trucks = new HashSet<>();

    public User() {
    }
  
    public User(String username, String email, String password) {
      this.email = email;
      this.password = password;
    }
  

    
}