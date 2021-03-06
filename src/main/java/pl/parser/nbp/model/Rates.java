package pl.parser.nbp.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * Rates tag from xml file
 * 
 * @author Krzysztof
 *
 */
public class Rates {
	
	private List<Rate> rates;

	@XmlElement(name = "Rate")
	public List<Rate> getRates() {
		return rates;
	}

	public void setRates(List<Rate> rates) {
		this.rates = rates;
	}

	
}
