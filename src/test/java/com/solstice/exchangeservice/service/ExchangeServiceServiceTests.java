package com.solstice.exchangeservice.service;

import com.solstice.exchangeservice.data.ExchangeServiceRepository;
import com.solstice.exchangeservice.exception.ResourceAlreadyExistsException;
import com.solstice.exchangeservice.model.ExchangeRate;
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

@WebMvcTest(ExchangeServiceService.class)
@RunWith(SpringRunner.class)
public class ExchangeServiceServiceTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ExchangeServiceService exchangeServiceService;

    @MockBean
    ExchangeServiceRepository exchangeServiceRepository;

    @Test
    public void serviceTest_USDTOINR_Success() {

        given(exchangeServiceRepository.findByFromCurrencyAndToCurrency(Mockito.anyString(), Mockito.anyString()))
                .willReturn(new ExchangeRate("USD", "INR", 72.00));

        ExchangeRate exchangeRate =
                exchangeServiceService.getExchangeRate("USD", "INR");

        Assert.assertEquals(72.00, exchangeRate.getConversion(), 0);
        Assert.assertEquals("USD", exchangeRate.getFromCurrency());
        Assert.assertEquals("INR", exchangeRate.getToCurrency());
    }

    @Test
    public void serviceTest_INRTOUSD_getExchangeRate_Success() {

        given(exchangeServiceRepository.findByFromCurrencyAndToCurrency(Mockito.anyString(), Mockito.anyString()))
                .willReturn(new ExchangeRate( "INR", "USD", 86.00));

        ExchangeRate exchangeRate =
                exchangeServiceService.getExchangeRate("INR", "USD");

        Assert.assertEquals(86.00, exchangeRate.getConversion(), 0);
        Assert.assertEquals("INR", exchangeRate.getFromCurrency());
        Assert.assertEquals("USD", exchangeRate.getToCurrency());
    }

    @Test
    public void serviceTest_addExchangeRate_add(){

        ExchangeRate r = new ExchangeRate("USD", "INR", 86.00);

        given(exchangeServiceRepository.findByFromCurrencyAndToCurrency(anyString(), anyString()))
                .willReturn(null);
        given(exchangeServiceRepository.save(any())).willReturn(null);

        exchangeServiceService.addExchangeRate(r);
    }

    @Test(expected = ResourceAlreadyExistsException.class)
    public void serviceTest_addExchangeRate_Failure(){

        ExchangeRate r = new ExchangeRate("USD", "INR", 86.00);

        given(exchangeServiceRepository.findByFromCurrencyAndToCurrency(anyString(), anyString()))
                .willReturn(r);

        exchangeServiceService.addExchangeRate(r);
    }
}
