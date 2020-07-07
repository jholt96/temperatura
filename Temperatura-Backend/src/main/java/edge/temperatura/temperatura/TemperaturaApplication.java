/*
Author: Josh Holt
Temperatura Backend version 0.9
Versions: Spring Boot 2.3, Java 11.

TODO Check Websocket Auth is working Correctly write tests. 
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
