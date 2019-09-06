package com.solstice.exchangeservice.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@JsonIgnoreProperties({"stackTrace", "localizedMessage", "suppressed", "cause"})
@JsonPropertyOrder({"message", "fromCurrency", "toCurrency", "conversion"})
@Getter
public class ResourceAlreadyExistsException extends RuntimeException {

    private String toCurrency;
    private String fromCurrency;
    private Double conversion;

    public ResourceAlreadyExistsException(String message, String fromCurrency, String toCurrency, Double conversion) {
        super(message);
        this.toCurrency = toCurrency;
        this.fromCurrency = fromCurrency;
        this.conversion = conversion;
    }
}
