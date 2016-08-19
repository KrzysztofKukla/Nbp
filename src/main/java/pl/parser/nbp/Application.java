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
	private NbpData nbpData;
	public Application() {
		nbpData = new NbpData();
	}
	public void start(String[] args){
		nbpData.setParameters(args);
	}
	@Override
	public void run(String... args) throws Exception {
		new Application().start(args);
	}
}
