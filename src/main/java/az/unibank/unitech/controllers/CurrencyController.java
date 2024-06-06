package az.unibank.unitech.controllers;

import az.unibank.unitech.dto.request.CurrencyRateRequest;
import az.unibank.unitech.dto.response.CommonResponse;
import az.unibank.unitech.services.AccountService;
import az.unibank.unitech.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;

import static az.unibank.unitech.enums.ResponseEnum.UNKNOWN_ERROR;

@RestController
@EnableScheduling
@RequestMapping(value = {"/api/public/currency"})
public class CurrencyController {

    @Autowired
    CurrencyService currencyService;

    @RequestMapping(value = "/getRate", method = RequestMethod.POST)
    public CommonResponse getAllAccounts(@RequestBody @Valid CurrencyRateRequest currencyRateRequest) {
        try{
            return currencyService.getRate(currencyRateRequest);
        }
        catch (Exception e){
            //LOG ERROR HERE. LOG4J or other
            System.out.println(e);
            return new CommonResponse(UNKNOWN_ERROR.getCode(), UNKNOWN_ERROR.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }
    }

}
