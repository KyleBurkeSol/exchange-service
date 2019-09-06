package com.solstice.exchangerate;

import com.solstice.exchangerate.data.ExchangeRateRepository;
import com.solstice.exchangerate.model.ExchangeRate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class ExchangeRateIntegrationTest {

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	ExchangeRateRepository exchangeRateRepository;
	@Autowired
	MockMvc mockMvc;

	@Test
	public void success() {
		//arrange
		//act

		exchangeRateRepository.save(new ExchangeRate("USD",
				"INR",86.00));

		ResponseEntity<ExchangeRate> exchangeRateResponse = restTemplate
				.getForEntity("http://localhost:8080/exchange-rate?from=USD&to=INR", ExchangeRate.class);

		//assert
		Assert.assertEquals(HttpStatus.OK, exchangeRateResponse.getStatusCode());
		Assert.assertEquals("USD", exchangeRateResponse.getBody().getFromCurrency());
		Assert.assertEquals("INR", exchangeRateResponse.getBody().getToCurrency());
		Assert.assertEquals(86.00, exchangeRateResponse.getBody().getConversion(), 0);
	}

	@Test
	public void swagger_API_success() throws Exception {
		ResponseEntity<Object> exchangeRateResponse = restTemplate
				.getForEntity("http://localhost:8080/v2/api-docs", Object.class);

		//assert
		Assert.assertEquals(HttpStatus.OK, exchangeRateResponse.getStatusCode());
	}

	@Test
	public void swagger_UI_success() throws Exception {
		ResponseEntity<String> exchangeRateResponse = restTemplate
				.getForEntity("http://localhost:8080/swagger-ui.html", String.class);

		//assert
		Assert.assertEquals(HttpStatus.OK, exchangeRateResponse.getStatusCode());
	}
}
