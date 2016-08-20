package pl.parser.nbp;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * Main application class
 * 
 * @author Krzysztof
 *
 */
@Component
public class Application implements CommandLineRunner{
	
	private NbpCurrencyRate nbpData;
	public Application() {
		nbpData = new NbpCurrencyRate();
	}
	public void start(String[] args){
		nbpData.calculateRates(args);
	}
	@Override
	public void run(String... args) throws Exception {
		new Application().start(args);
	}
}
