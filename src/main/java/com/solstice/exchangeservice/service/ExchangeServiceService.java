package com.solstice.exchangeservice.service;

import com.solstice.exchangeservice.data.ExchangeServiceRepository;
import com.solstice.exchangeservice.exception.ExchangeRateNotFoundException;
import com.solstice.exchangeservice.exception.ResourceAlreadyExistsException;
import com.solstice.exchangeservice.model.ExchangeRate;
import org.springframework.stereotype.Service;

@Service
public class ExchangeServiceService {

	final ExchangeServiceRepository exchangeServiceRepository;

	public ExchangeServiceService(ExchangeServiceRepository exchangeServiceRepository) {
		this.exchangeServiceRepository = exchangeServiceRepository;
	}

	public ExchangeRate getExchangeRate(String from, String to) {
		String message = "Exchange Rate Not Found";

		//Call the repo interface method
		ExchangeRate exchangeRate = exchangeServiceRepository
				.findByFromCurrencyAndToCurrency(from, to);

		if(exchangeRate ==null){
			throw new ExchangeRateNotFoundException(message, from, to);
		}
		return exchangeRate;
	}

	public void addExchangeRate(ExchangeRate response) {
		ExchangeRate r = exchangeServiceRepository
				.findByFromCurrencyAndToCurrency(response.getFromCurrency(), response.getToCurrency());

		if(r == null){
			exchangeServiceRepository.save(response);
		} else {
			throw new ResourceAlreadyExistsException("Value already exists", r.getFromCurrency(), r.getToCurrency(), r.getConversion());
		}
	}
}
