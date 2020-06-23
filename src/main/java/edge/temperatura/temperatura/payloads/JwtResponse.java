/*
Author: Josh Holt
Temperatura Backend 
Versions: Spring Boot 2.3, Java 11.

Purpose of Class: Provide a POJO for the JWT response. 
When user logs in this object will be returned if they are successfully authenticated. 
*/
package edge.temperatura.temperatura.payloads;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

	private String token;
	private String type = "Bearer";
	private String username;
    private List<String> roles;

    public JwtResponse(String accessToken, String username, List<String> roles) {
		this.token = accessToken;
		this.username = username;
		this.roles = roles;
	}
    
}