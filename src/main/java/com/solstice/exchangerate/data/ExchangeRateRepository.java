package com.solstice.exchangerate.data;

import com.solstice.exchangerate.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    ExchangeRate findByFromCurrencyAndToCurrency(String from, String to);
}
