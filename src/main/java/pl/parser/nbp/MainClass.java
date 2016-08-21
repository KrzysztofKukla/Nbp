package pl.parser.nbp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Runner class of Spring Boot Application
 * 
 * array args needs 3 correct parameters
 * 
 * @author Krzysztof
 *
 */
@SpringBootApplication
public class MainClass {
	public static void main(String[] args) {
		SpringApplication.run(MainClass.class, args);
	}
}
