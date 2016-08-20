package pl.parser.nbp.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ExchangeRatesSeries")
public class CurrencyRates {
	private String currency;
	private Rates rate;

	@XmlElement(name = "Currency")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@XmlElement(name = "Rates")
	public Rates getRate() {
		return rate;
	}

	public void setRate(Rates rate) {
		this.rate = rate;
	}

	
	

}
