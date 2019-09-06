package com.solstice.exchangerate.data;

import com.solstice.exchangerate.model.ExchangeRate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ExchangeRateRepositoryTests {

    @Autowired
    public ExchangeRateRepository exchangeRateRepository;

    @Test
    public void findByFromAndTo_Success(){

        ExchangeRate exchangeRate =
                new ExchangeRate("INR","USD",1/72.00);
        ExchangeRate rateResponse = exchangeRateRepository.save(exchangeRate);

        ExchangeRate exchangeRate1 = exchangeRateRepository
                .findByFromCurrencyAndToCurrency("INR", "USD");

        Assert.assertEquals(1/72.00, exchangeRate1.getConversion(), 0);
        Assert.assertEquals("INR", exchangeRate1.getFromCurrency());
        Assert.assertEquals("USD", exchangeRate1.getToCurrency());
    }


}
