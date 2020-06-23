/*
Author: Josh Holt
Temperatura Backend
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: Provide a POJO for the login request. 
When user logs in they will pass this object in the POST request. 
*/
package edge.temperatura.temperatura.payloads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Signin {

    private String username;
     
    private String password;
    
}