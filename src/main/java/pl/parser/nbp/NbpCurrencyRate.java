package pl.parser.nbp;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import pl.parser.nbp.enums.Currency;
import pl.parser.nbp.model.CurrencyRates;
import pl.parser.nbp.model.Rate;

/**
 * Service calculates nbp currency rates
 * 
 * @author Krzysztof
 *
 */
@Service
public class NbpCurrencyRate {

	private static final Logger LOGGER = Logger.getLogger(NbpCurrencyRate.class);
	
	private static final String MAIN_URL = "http://api.nbp.pl/api/exchangerates/rates";
	private static final String TABLE_TYPE = "C";
	private static final String FORMAT_XML = "?format=xml";
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.####");
	
	@Autowired
	private RestTemplate restTemplate;
	
	private Currency currencySymbol;
	private LocalDate startDate;
	private LocalDate endDate;
	
	/**
	 * Method gets arguments and calculates rates
	 * 
	 * @param args
	 */
	public void getCalculatedRates(String args[]){
		setParameters(args);
		List<Rate> rates = getCurrencyRates(currencySymbol, startDate, endDate);
		if (rates.isEmpty()) throw new RuntimeException("Empty file");
		double averageRate = calculateAverageBidRateValue(rates);
		double standardDeviation = calculateStandardDeviation(rates);
		showResults(averageRate, standardDeviation);
	}
	
	/**
	 * Method checks given arguments and sets field if they are correct
	 * 
	 * @param args
	 */
	private void setParameters(String args[]){
		if (args.length!=3){
			LOGGER.error("Wrong arguments");
			throw new IllegalArgumentException("Incorrect number of arguments");
		}
		currencySymbol = Currency.valueOf(args[0]);
		startDate = LocalDate.parse(args[1]);
		endDate = LocalDate.parse(args[2]);
		if (startDate.compareTo(endDate)>0 ){
			LOGGER.error("Incorrect arguments");
			throw new IllegalArgumentException("Start date is greater than end date");
		}
		LOGGER.info("Parameters have been set" );
	}
	
	/**
	 * Method gets data from external service of nbp 
	 * 
	 * @param currency
	 * @param startDate
	 * @param endDate
	 * @return list of 'Rate' tags
	 */
	private List<Rate> getCurrencyRates(Currency currency, LocalDate startDate, LocalDate endDate){
		String url = String.join("/", MAIN_URL, TABLE_TYPE, currency.toString(), startDate.toString(), endDate.toString(), FORMAT_XML);
		
		ResponseEntity<CurrencyRates> response = restTemplate.getForEntity(url, CurrencyRates.class);
		if (response.getStatusCode()==HttpStatus.OK){
			LOGGER.info("Currency rates have been collected");
			return response.getBody().getRate().getRates();
		}
		LOGGER.warn("Cannot get currency rates, HttpStatusCode: "+response.getStatusCode());
		return Collections.emptyList();
	}
	
	/**
	 * Method calculates average currency value for given arguments
	 * 
	 * @param rates
	 * @return average currency value
	 */
	private double calculateAverageBidRateValue(List<Rate> rates){
		OptionalDouble averageOptionalDouble = rates.stream().mapToDouble((Rate r) -> Double.parseDouble(r.getBid())).average();
		LOGGER.info("Calculation of average rate value...");
		return averageOptionalDouble.isPresent() ? averageOptionalDouble.getAsDouble() : 0;
	}
	
	/**
	 * Methods calculates standard deviation
	 * 
	 * @param rates
	 * @return standard deviation value
	 */
	private double calculateStandardDeviation(List<Rate> rates){
		StandardDeviation standardDeviation = new StandardDeviation();
		double[] askArray = new double[rates.size()];
		for(int i=0;i<rates.size();i++){
			askArray[i] = Double.parseDouble(rates.get(i).getAsk());
		}
		LOGGER.info("Calculation the standard deviation...");
		return standardDeviation.evaluate(askArray);
	}
	
	/**
	 * Method presents results on console
	 * 
	 * @param averageRate
	 * @param standardDeviation
	 */
	private void showResults(double averageRate, double standardDeviation) {
		System.out.println("Waluta: "+currencySymbol);
		System.out.println("Przedział czasu: " + startDate + " - " + endDate);
		System.out.println("Średni kurs kupna: " + DECIMAL_FORMAT.format(averageRate));
		System.out.println("Odchylenie standardowe kursów sprzedaży: " + DECIMAL_FORMAT.format(standardDeviation));
	}

	public Currency getCurrencySymbol() {
		return currencySymbol;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	
}
