package com.solstice.exchangerate.service;

import com.solstice.exchangerate.data.ExchangeRateRepository;
import com.solstice.exchangerate.exception.ExchangeRateNotFoundException;
import com.solstice.exchangerate.exception.ResourceAlreadyExistsException;
import com.solstice.exchangerate.model.ExchangeRate;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateService {

	final ExchangeRateRepository exchangeRateRepository;

	public ExchangeRateService(ExchangeRateRepository exchangeRateRepository) {
		this.exchangeRateRepository = exchangeRateRepository;
	}

	public ExchangeRate getExchangeRate(String from, String to) {
		String message = "Exchange Rate Not Found";

		//Call the repo interface method
		ExchangeRate exchangeRate = exchangeRateRepository
				.findByFromCurrencyAndToCurrency(from, to);

		if(exchangeRate ==null){
			throw new ExchangeRateNotFoundException(message, from, to);
		}
		return exchangeRate;
	}

	public void addExchangeRate(ExchangeRate response) {
		ExchangeRate r = exchangeRateRepository
				.findByFromCurrencyAndToCurrency(response.getFromCurrency(), response.getToCurrency());

		if(r == null){
			exchangeRateRepository.save(response);
		} else {
			throw new ResourceAlreadyExistsException("Value already exists", r.getFromCurrency(), r.getToCurrency(), r.getConversion());
		}
	}
}
