package com.solstice.exchangerate.service;

import com.solstice.exchangerate.data.ExchangeRateRepository;
import com.solstice.exchangerate.exception.ResourceAlreadyExistsException;
import com.solstice.exchangerate.model.ExchangeRate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@WebMvcTest(ExchangeRateService.class)
@RunWith(SpringRunner.class)
public class ExchangeRateServiceTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ExchangeRateService exchangeRateService;

    @MockBean
    ExchangeRateRepository exchangeRateRepository;

    @Test
    public void serviceTest_USDTOINR_Success() {

        given(exchangeRateRepository.findByFromCurrencyAndToCurrency(Mockito.anyString(), Mockito.anyString()))
                .willReturn(new ExchangeRate("USD", "INR", 72.00));

        ExchangeRate exchangeRate =
                exchangeRateService.getExchangeRate("USD", "INR");

        Assert.assertEquals(72.00, exchangeRate.getConversion(), 0);
        Assert.assertEquals("USD", exchangeRate.getFromCurrency());
        Assert.assertEquals("INR", exchangeRate.getToCurrency());
    }

    @Test
    public void serviceTest_INRTOUSD_getExchangeRate_Success() {

        given(exchangeRateRepository.findByFromCurrencyAndToCurrency(Mockito.anyString(), Mockito.anyString()))
                .willReturn(new ExchangeRate( "INR", "USD", 1/72.00));

        ExchangeRate exchangeRate =
                exchangeRateService.getExchangeRate("INR", "USD");

        Assert.assertEquals(1/72.00, exchangeRate.getConversion(), 0);
        Assert.assertEquals("INR", exchangeRate.getFromCurrency());
        Assert.assertEquals("USD", exchangeRate.getToCurrency());
    }

    @Test
    public void serviceTest_addExchangeRate_add(){

        ExchangeRate r = new ExchangeRate("USD", "INR", 72.00);

        given(exchangeRateRepository.findByFromCurrencyAndToCurrency(anyString(), anyString()))
                .willReturn(null);
        given(exchangeRateRepository.save(any())).willReturn(null);

        exchangeRateService.addExchangeRate(r);
    }

    @Test(expected = ResourceAlreadyExistsException.class)
    public void serviceTest_addExchangeRate_Failure(){

        ExchangeRate r = new ExchangeRate("USD", "INR", 72.00);

        given(exchangeRateRepository.findByFromCurrencyAndToCurrency(anyString(), anyString()))
                .willReturn(r);

        exchangeRateService.addExchangeRate(r);
    }
}
