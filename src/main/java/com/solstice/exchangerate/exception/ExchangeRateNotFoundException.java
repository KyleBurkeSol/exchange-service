package com.solstice.exchangerate.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@JsonIgnoreProperties({"stackTrace", "localizedMessage", "suppressed", "cause"})
@JsonPropertyOrder({"message", "fromCurrency", "toCurrency"})
@Getter
public class ExchangeRateNotFoundException extends RuntimeException {
    private String toCurrency;
    private String fromCurrency;

    public ExchangeRateNotFoundException(String message, String fromCurrency, String toCurrency) {
        super(message);
        this.toCurrency = toCurrency;
        this.fromCurrency = fromCurrency;
    }
}
