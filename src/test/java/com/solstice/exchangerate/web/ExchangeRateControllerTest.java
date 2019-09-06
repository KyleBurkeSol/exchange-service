package com.solstice.exchangerate.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solstice.exchangerate.model.ExchangeRate;
import com.solstice.exchangerate.exception.ExchangeRateNotFoundException;
import com.solstice.exchangerate.service.ExchangeRateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.TransactionSystemException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExchangeRateController.class)
@RunWith(SpringRunner.class)
public class ExchangeRateControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ExchangeRateService exchangeRateService;

	@Test
	public void controllerTest_USDToINR() throws Exception {

		//arrange
		given(exchangeRateService.getExchangeRate(anyString(), anyString()))
				.willReturn(new ExchangeRate("USD", "INR", 72.0));

		//act
		mockMvc.perform(MockMvcRequestBuilders.get("/exchange-rate?from=USD&to=INR"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("fromCurrency").value("USD"))
				.andExpect(jsonPath("toCurrency").value("INR"))
				.andExpect(jsonPath("conversion").value(72.0));
		//assert
	}

	@Test
	public void controllerTest_INRToUSD() throws Exception {

		//arrange
		given(exchangeRateService.getExchangeRate(anyString(), anyString()))
				.willReturn(new ExchangeRate("INR", "USD", 72.0));

		//act
		mockMvc.perform(MockMvcRequestBuilders.get("/exchange-rate?from=INR&to=USD"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("fromCurrency").value("INR"))
				.andExpect(jsonPath("toCurrency").value("USD"))
				.andExpect(jsonPath("conversion").value(72.0));
		//assert
	}

	@Test
	public void controllerTest_fromNotFound() throws Exception {

		//arrange
		given(exchangeRateService.getExchangeRate(anyString(), anyString()))
				.willThrow(new ExchangeRateNotFoundException("Exchange Rate Not Found", "USA", "INR"));

		//act
		mockMvc.perform(MockMvcRequestBuilders.get("/exchange-rate?from=USA&to=INR"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("message").value("Exchange Rate Not Found"))
				.andExpect(jsonPath("fromCurrency").value("USA"))
				.andExpect(jsonPath("toCurrency").value("INR"));

	}

	@Test
	public void missingRequestParam() throws Exception{

		//act

		mockMvc.perform(MockMvcRequestBuilders.get("/exchange-rate?from=AUD"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void addCurrency() throws Exception {
		willDoNothing().given(exchangeRateService).addExchangeRate(any());

		String jsonBody = new ObjectMapper().writeValueAsString(new ExchangeRate("USD", "INR", 77.0));

		mockMvc.perform(MockMvcRequestBuilders.post("/exchange-rate")
				.content(jsonBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("message").value("success"));
	}

	@Test
	public void addCurrency_failureMissingBody() throws Exception{

		willThrow(HttpMessageNotReadableException.class).given(exchangeRateService).addExchangeRate(any());

		String jsonBody = new ObjectMapper().writeValueAsString(new ExchangeRate("USD", "INR", 77.0));

		mockMvc.perform(MockMvcRequestBuilders.post("/exchange-rate")
				.content(jsonBody).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void addCurrency_failureMissingFieldsInBody() throws Exception{

		willThrow(TransactionSystemException.class).given(exchangeRateService).addExchangeRate(any());

		mockMvc.perform(MockMvcRequestBuilders.post("/exchange-rate")
				.content("{\n" +
							"\t\"fromCurrency\": \"USD\"\n" +
						"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
}
