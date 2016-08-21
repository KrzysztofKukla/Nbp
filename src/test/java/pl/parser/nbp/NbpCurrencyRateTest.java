package pl.parser.nbp;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import pl.parser.nbp.enums.Currency;
import pl.parser.nbp.model.CurrencyRates;

/**
 * Test class
 * 
 * @author Krzysztof
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class NbpCurrencyRateTest {
	@InjectMocks
	private NbpCurrencyRate nbp = new NbpCurrencyRate();
	
	@Mock
	private RestTemplate restTemplate = new RestTemplate();
	
	private String correctCurrencySymbol = "USD";
	private String correctStartDate = "2014-05-20";
	private String correctEndDate = "2014-05-25";
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	
	@Test
	public void whenAmountOfParametersIsNotEqual3ThenIllegalArgumentException(){
		String[] args = new String[]{"oneSign"};
		exception.expect(IllegalArgumentException.class);
		nbp.getCalculatedRates(args);
	}
	
	@Test
	public void whenCurrencySymbolIsWrongThenIllegalArgumentException(){
		String[] args = new String[]{"xxx", correctStartDate, correctEndDate};
		exception.expect(IllegalArgumentException.class);
		nbp.getCalculatedRates(args);
	}
	
	@Test
	public void whenStartDateIsWrongThenThrowIllegalArgumentException(){
		String[] args = new String[]{correctCurrencySymbol, "16th of April 2103", correctEndDate};
		exception.expect(DateTimeParseException.class);
		nbp.getCalculatedRates(args);
	}
	
	@Test
	public void whenEndDateIsIncorrectThenThrowIllegalArgumentException(){
		String[] args = new String[]{correctCurrencySymbol, correctStartDate, "10th of May 2013"};
		exception.expect(DateTimeParseException.class);
		nbp.getCalculatedRates(args);
	}
	
	@Test
	public void whenStartDateIsGreaterThanEndDateThenThrowIllegalArgumentException(){
		String startDate = "2013-04-23";
		String endDate = "2013-04-22";
		String[] args = new String[]{correctCurrencySymbol, startDate, endDate};
		exception.expect(IllegalArgumentException.class);
		nbp.getCalculatedRates(args);
	}
	
	@Test
	public void whenInputParametersAreOkThenNbpDataFielsAreSet(){
		String MAIN_URL = "http://api.nbp.pl/api/exchangerates/rates";
		String TABLE_TYPE = "C";
		String FORMAT_XML = "?format=xml";
		String url = String.join("/", MAIN_URL, TABLE_TYPE, correctCurrencySymbol,correctStartDate, correctEndDate, FORMAT_XML);
		RestTemplate rest = new RestTemplate();
		ResponseEntity<CurrencyRates> temp = rest.getForEntity(url, CurrencyRates.class);
		when(restTemplate.getForEntity(url, CurrencyRates.class)).thenReturn(temp);
		String[] args = new String[]{correctCurrencySymbol, correctStartDate, correctEndDate};
		nbp.getCalculatedRates(args);
		assertEquals(nbp.getCurrencySymbol(), Currency.USD);
		assertEquals(nbp.getStartDate(), LocalDate.parse(correctStartDate));
		assertEquals(nbp.getEndDate(), LocalDate.parse(correctEndDate));
	}
	
	@Test
	public void whenUrlAddressIsWrongThenThrowIllegalStateException(){
		String url = "http://api.nbp.pl/api/exchangerates/rates/sss";
		RestTemplate restTemplate = new RestTemplate();
		exception.expect(RestClientException.class);
		restTemplate.getForEntity(url, CurrencyRates.class);
	}
	
	
}
