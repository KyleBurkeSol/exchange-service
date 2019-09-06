package com.solstice.exchangerate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long id;

	@NotNull
    private String fromCurrency;

	@NotNull
	private String toCurrency;

	@NotNull
	private Double conversion;

	public ExchangeRate(String fromCurrency, String toCurrency, Double conversion) {
		this.fromCurrency = fromCurrency;
		this.toCurrency = toCurrency;
		this.conversion = conversion;
	}
}
