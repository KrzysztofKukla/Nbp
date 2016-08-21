package pl.parser.nbp;



import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 *Command Line Runner implementation
 * 
 * @author Krzysztof
 *
 */
@Component
public class StartupRunner implements CommandLineRunner{
	
	private final Logger LOGGER = Logger.getLogger(StartupRunner.class);
	
	@Autowired
	private NbpCurrencyRate nbpRate;
	
	@Override
	public void run(String... args){
		try{
			nbpRate.getCalculatedRates(args);
		}
		catch(Exception e){
			LOGGER.error("Wrong arguments!", e);
		}
	}
}
