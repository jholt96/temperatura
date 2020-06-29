/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: Provide a POJO for the signup request. 
When an ADMIN creates a new user this is the object they will create in the form. 
*/

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