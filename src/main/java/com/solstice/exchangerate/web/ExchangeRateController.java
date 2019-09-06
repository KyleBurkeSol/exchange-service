package com.solstice.exchangerate.web;

import com.solstice.exchangerate.model.ExchangeRate;
import com.solstice.exchangerate.model.GenericResponse;
import com.solstice.exchangerate.service.ExchangeRateService;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "Exchange Service Controller")
public class ExchangeRateController {

	private final ExchangeRateService exchangeServiceService;

	public ExchangeRateController(ExchangeRateService exchangeServiceService) {
		this.exchangeServiceService = exchangeServiceService;
	}

	@GetMapping("/exchange-rate")
	public ExchangeRate getExchangeRate(@RequestParam String from, @RequestParam String to) {

		return exchangeServiceService.getExchangeRate(from, to);
	}

	@PostMapping("/exchange-rate")
	@ResponseStatus(HttpStatus.CREATED)
	public GenericResponse addExchangeRate(@RequestBody ExchangeRate rateResponse){
		exchangeServiceService.addExchangeRate(rateResponse);
		return new GenericResponse("success");
	}
}
