package pl.parser.nbp;

import java.time.LocalDate;

import static org.junit.Assert.*;
import org.junit.*;
import org.junit.rules.ExpectedException;

import pl.parser.nbp.enums.Currency;


public class NbpDataTest {
	private NbpData nbp;
	private String correctCurrencySymbol = "USD";
	private String correctStartDate = "2013-04-16";
	private String correctEndDate = "2013-05-10";
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void before(){
		nbp = new NbpData();
	}
	
	@Test
	public void whenAmountOfParametersIsNotEqual3ThenThrowIllegalArgumentException(){
		String[] args = new String[]{"oneSign"};
		exception.expect(IllegalArgumentException.class);
		nbp.setParameters(args);
	}
	
	@Test
	public void whenCurrencySymbolIsIncorrectThenThrowIllegalArgumentException(){
		String[] args = new String[]{"xxx", correctStartDate, correctEndDate};
		exception.expect(IllegalArgumentException.class);
		nbp.setParameters(args);
	}
	
	@Test
	public void whenStartDateIsIncorrectThenThrowIllegalArgumentException(){
		String[] args = new String[]{correctCurrencySymbol, "16th of April 2103", correctEndDate};
		exception.expect(IllegalArgumentException.class);
		nbp.setParameters(args);
	}
	
	@Test
	public void whenEndDateIsIncorrectThenThrowIllegalArgumentException(){
		String[] args = new String[]{correctCurrencySymbol, correctStartDate, "10th of May 2013"};
		exception.expect(IllegalArgumentException.class);
		nbp.setParameters(args);
	}
	
	@Test
	public void whenStartDateIsGreaterThanEndDateThenThrowIllegalArgumentException(){
		String startDate = "2013-04-23";
		String endDate = "2013-04-22";
		String[] args = new String[]{correctCurrencySymbol, startDate, endDate};
		exception.expect(IllegalArgumentException.class);
		nbp.setParameters(args);
	}
	
	@Test
	public void whenInputParametersAreOkThenNbpDataFielsAreSet(){
		String[] args = new String[]{correctCurrencySymbol, correctStartDate, correctEndDate};
		nbp.setParameters(args);
		assertEquals(nbp.getCurrencySymbol(), Currency.USD);
		assertEquals(nbp.getStartDate(), LocalDate.parse(correctStartDate));
		assertEquals(nbp.getEndDate(), LocalDate.parse(correctEndDate));
	}
	
}
