package pl.parser.nbp;

import java.time.LocalDate;

import pl.parser.nbp.enums.Currency;

public class NbpCurrencyRate {
	private Currency currencySymbol;
	private LocalDate startDate;
	private LocalDate endDate;
	public void setParameters(String args[]){
		try{
			if (args.length!=3) throw new IllegalArgumentException("Incorrect number of arguments");
			currencySymbol = Currency.valueOf(args[0]);
			startDate = LocalDate.parse(args[1]);
			endDate = LocalDate.parse(args[2]);
			if (startDate.compareTo(endDate)>0 ) throw new IllegalArgumentException("Start date is greater than end date");
		}
		catch(Exception e){
			throw new IllegalArgumentException("Incorrect arguments", e);
		}
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
