package edge.temperatura.temperatura.payloads;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Signup {

    private String username;
 
    private String email;
    
    private Set<String> roles;
    
    private String password;
  
}