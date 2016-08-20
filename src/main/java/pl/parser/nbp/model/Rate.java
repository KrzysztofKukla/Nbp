package pl.parser.nbp.model;

import javax.xml.bind.annotation.XmlElement;

public class Rate {
	private String EffectiveDate;
	private String Bid;
	private String Ask;
	
	@XmlElement(name = "EffectiveDate")
	public String getEffectiveDate() {
		return EffectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		EffectiveDate = effectiveDate;
	}
	@XmlElement(name = "Bid")
	public String getBid() {
		return Bid;
	}
	public void setBid(String bid) {
		Bid = bid;
	}
	@XmlElement(name = "Ask")
	public String getAsk() {
		return Ask;
	}
	public void setAsk(String ask) {
		Ask = ask;
	}
	
}
