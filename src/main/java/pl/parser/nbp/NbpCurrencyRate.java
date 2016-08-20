package pl.parser.nbp;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import pl.parser.nbp.enums.Currency;
import pl.parser.nbp.model.CurrencyRates;
import pl.parser.nbp.model.Rate;

public class NbpCurrencyRate {

	private static final Logger LOGGER = Logger.getLogger(NbpCurrencyRate.class);
	
	private static final String MAIN_URL = "http://api.nbp.pl/api/exchangerates/rates/";
	private static final String TABLE_TYPE = "C";
	private static final String FORMAT_XML = "?format=xml";
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.####");
	
	private Currency currencySymbol;
	private LocalDate startDate;
	private LocalDate endDate;
	
	public void calculateRates(String args[]){
		getCurrencyRates(args);
		List<Rate> rates = getCurrencyRates();
		double averageRate = calculateAverageRateValue(rates);
		double standardDeviation = calculateStandardDeviation(rates);
		showResults(averageRate, standardDeviation);
	}
	private void showResults(double averageRate, double standardDeviation) {
		System.out.println("Waluta: "+currencySymbol);
		System.out.println("Przedział czasu: " + startDate + " - " + endDate);
		System.out.println("Średni kurs kupna: " + DECIMAL_FORMAT.format(averageRate));
		System.out.println("Odchylenie standardowe kupnów sprzedaży: " + DECIMAL_FORMAT.format(standardDeviation));
	}
	private void getCurrencyRates(String args[]){
		try{
			if (args.length!=3){
				LOGGER.error("Incorrect arguments");
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
		catch(Exception e){
			LOGGER.error("Incorrect arguments", e);
			throw new IllegalArgumentException("Incorrect arguments", e);
		}
	}
	
	private List<Rate> getCurrencyRates(){
		String url = String.join("/", MAIN_URL, TABLE_TYPE, currencySymbol.toString(), startDate.toString(), endDate.toString(), FORMAT_XML);
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> responseString = restTemplate.getForEntity(url, String.class);
		System.out.println(responseString);
		
		ResponseEntity<CurrencyRates> response = restTemplate.getForEntity(url, CurrencyRates.class);
		if (response.getStatusCode()==HttpStatus.OK){
			LOGGER.info("Currency rates have been collected");
			return response.getBody().getRate().getRates();
		}
		LOGGER.warn("Cannot get currency rates. HttpStatus: "+response.getStatusCode());
		return Collections.emptyList();
	}
	
	private double calculateAverageRateValue(List<Rate> rates){
		OptionalDouble averageOptionalDouble = rates.stream().mapToDouble((Rate r) -> Double.parseDouble(r.getBid())).average();
		LOGGER.info("Evaluating the average rate value...");
		return averageOptionalDouble.isPresent() ? averageOptionalDouble.getAsDouble() : 0;
	}
	
	private double calculateStandardDeviation(List<Rate> rates){
		StandardDeviation standardDeviation = new StandardDeviation();
		double[] askArray = new double[rates.size()];
		for(int i=0;i<rates.size();i++){
			askArray[i] = Double.parseDouble(rates.get(i).getAsk());
		}
		LOGGER.info("Evaluating the standard deviation...");
		return standardDeviation.evaluate(askArray);
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
