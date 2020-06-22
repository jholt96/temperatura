/*
Author: Josh Holt
Temperatura Backend version 1.0.0
Versions: Spring Boot 2.3, Java 11.

TODO Add documentation, add feature for users to change username and password, more error handling, fetch crfs token
*/

package edge.temperatura.temperatura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TemperaturaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemperaturaApplication.class, args);
	}
}
