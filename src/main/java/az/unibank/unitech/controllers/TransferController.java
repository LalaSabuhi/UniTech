package az.unibank.unitech.controllers;

import az.unibank.unitech.dto.request.TransferRequest;
import az.unibank.unitech.dto.response.CommonResponse;
import az.unibank.unitech.entities.User;
import az.unibank.unitech.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;

import static az.unibank.unitech.enums.ResponseEnum.*;

@RestController
@RequestMapping(value = {"/api/transfers"})
public class TransferController {

    @Autowired
    TransferService transferService;

    @RequestMapping(value = "/makeTransfer", method = RequestMethod.POST)
    public CommonResponse transfer(@RequestBody @Valid TransferRequest transferRequest) {
        try{
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return transferService.makeTransfer(user.getId(), transferRequest.getFromIdentifier(), transferRequest.getToIdentifier(), transferRequest.getAmount());
        }
        catch (Exception e){
            //LOGGING HERE
            System.out.println(e);
            return new CommonResponse(UNKNOWN_ERROR.getCode(), UNKNOWN_ERROR.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }
    }

}
