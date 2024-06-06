package az.unibank.unitech.controllers;

import az.unibank.unitech.dto.request.AddBalanceRequest;
import az.unibank.unitech.dto.response.CommonResponse;
import az.unibank.unitech.entities.User;
import az.unibank.unitech.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;

import static az.unibank.unitech.enums.ResponseEnum.AUTH_ERROR;
import static az.unibank.unitech.enums.ResponseEnum.UNKNOWN_ERROR;

@RestController
@EnableScheduling
@RequestMapping(value = {"/api/accounts"})
public class AccountController {

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResponse getAllAccounts() {
        try{
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return accountService.getUserEnabledAccounts(user.getId());
        }
        catch (Exception e){
            //LOG ERROR HERE
            System.out.println(e);
            return new CommonResponse(AUTH_ERROR.getCode(), AUTH_ERROR.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }
    }

    @RequestMapping(value = "/deactivate", method = RequestMethod.POST)
    public CommonResponse disableAccount(@RequestParam(defaultValue = "0") Long accountId) {
        try{
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user == null) return new CommonResponse(AUTH_ERROR.getCode(), AUTH_ERROR.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());

            return accountService.disableAccount(user.getId(), accountId);
        }
        catch (Exception e){
            //LOG ERROR HERE
            System.out.println(e);
            return new CommonResponse(UNKNOWN_ERROR.getCode(), UNKNOWN_ERROR.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }
    }

    @RequestMapping(value = "/addAccount", method = RequestMethod.POST)
    public CommonResponse addAccount() {
        try{
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user == null) return new CommonResponse(AUTH_ERROR.getCode(), AUTH_ERROR.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());

            return accountService.addAccount(user);
        }
        catch (Exception e){
            //LOG ERROR HERE
            System.out.println(e);
            return new CommonResponse(AUTH_ERROR.getCode(), AUTH_ERROR.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }
    }

    @RequestMapping(value = "/addBalance", method = RequestMethod.POST)
    public CommonResponse addBalance(@RequestBody @Valid AddBalanceRequest addBalanceRequest) {
        try{
            return accountService.addBalance(addBalanceRequest.getIdentifier().toUpperCase(), addBalanceRequest.getAmount());
        }
        catch (Exception e){
            //LOG ERROR HERE
            System.out.println(e);
            return new CommonResponse(UNKNOWN_ERROR.getCode(), UNKNOWN_ERROR.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }
    }

}
