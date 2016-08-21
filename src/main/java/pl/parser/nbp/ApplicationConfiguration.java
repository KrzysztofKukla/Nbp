package pl.parser.nbp;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Main configuration class
 * 
 * @author Krzysztof
 *
 */
@Configuration
public class ApplicationConfiguration{
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
