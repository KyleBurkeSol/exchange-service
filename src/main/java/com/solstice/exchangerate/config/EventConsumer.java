package com.solstice.exchangerate.config;

import com.solstice.exchangerate.model.ExchangeRate;
import com.solstice.exchangerate.service.ExchangeRateService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {
	@Autowired
	ExchangeRateService exchangeRateService;

	@RabbitListener(queues = "exchangeRateQueue")
	public void receive(ExchangeRate message) {
		exchangeRateService.addExchangeRate(message);
	}
}