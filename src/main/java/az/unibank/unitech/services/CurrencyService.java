package az.unibank.unitech.services;
import az.unibank.unitech.dto.request.CurrencyRateRequest;
import az.unibank.unitech.dto.response.CommonResponse;
import az.unibank.unitech.dto.response.CurrencyRatesResponse;
import az.unibank.unitech.models.CurrencyRates;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static az.unibank.unitech.enums.ResponseEnum.*;

@Service
public class CurrencyService {

    public static CurrencyRates currencyRates = new CurrencyRates();

    public CommonResponse getRate(CurrencyRateRequest currencyRateRequest){
        // CALL CACHEABLE METHOD
        CurrencyRates currencyRates = CurrencyService.currencyRates;

        // CHECK FOR EXISTING CURRENCY
        if (currencyRates.getRates().get(currencyRateRequest.getFrom().toLowerCase()) == null
                ||
                currencyRates.getRates().get(currencyRateRequest.getTo().toLowerCase()) == null){
            return new CommonResponse(CURRENCY_NOT_FOUND.getCode(), CURRENCY_NOT_FOUND.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }

        // RATE CALCULATING
        float rate = currencyRates.getRates().get(currencyRateRequest.getTo().toLowerCase())
                /
                currencyRates.getRates().get(currencyRateRequest.getFrom().toLowerCase());

        // CREATING
        CurrencyRatesResponse currencyRatesResponse = new CurrencyRatesResponse(currencyRates.getDateTime().toString(),
                currencyRateRequest.getFrom().toUpperCase() + "/" + currencyRateRequest.getTo().toUpperCase(),
                rate);
        return CommonResponse.success(currencyRatesResponse);
    }

    @Scheduled(fixedRate = 60*1000)
    public static void setRates(){
        // THIS METHOD SCHEDULED. 1 MINUTE.

        //WATCH THE DATETIME. IT REFRESHES EVERY Minute

        // WE CAN USE here rest requests to 3'rd party endpoints.

        // MOCK DATA. I could get data from CBAR.
        Map<String, Float> currencies = new HashMap<>();
        currencies.put("azn", 1.7F);
        currencies.put("usd", 1F);
        currencies.put("eur", 0.92F);
        currencies.put("rub", 133F);
        currencies.put("try", 14.77F);

        currencyRates.setRates(currencies);
        currencyRates.setDateTime(LocalDateTime.now());
    }
}
