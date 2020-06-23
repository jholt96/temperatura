/*
Author: Josh Holt
Temperatura Backend version 0.9
Versions: Spring Boot 2.3, Java 11.

TODO Clean up error Handling a bit more, Check Websocket Auth is working Correctly
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
