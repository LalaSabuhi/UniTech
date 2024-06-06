package az.unibank.unitech.services;
import az.unibank.unitech.dto.response.CommonResponse;
import az.unibank.unitech.entities.Account;
import az.unibank.unitech.entities.Transfer;
import az.unibank.unitech.repos.AccountRepository;
import az.unibank.unitech.repos.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static az.unibank.unitech.enums.ResponseEnum.*;

@Service
public class TransferService {

    @Autowired
    TransferRepository transferRepository;

    @Autowired
    AccountRepository accountRepository;

    public CommonResponse makeTransfer(long userId, String fromIdentifier, String toIdentifier, BigDecimal amount){
        if (amount.compareTo(BigDecimal.valueOf(1))<0){
            return new CommonResponse(SMALL_AMOUNT.getCode(), SMALL_AMOUNT.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }

        Optional<Account> fromAccountOptional = accountRepository.findAccountByIdentifierAndUser(fromIdentifier.toUpperCase(), userId);

        if (!fromAccountOptional.isPresent()){
            return new CommonResponse(NOT_YOUR_ACCOUNT_TRANSFER.getCode(), NOT_YOUR_ACCOUNT_TRANSFER.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }
        if (fromAccountOptional.get().getBalance().compareTo(amount)<0){
            return new CommonResponse(INSUFFICIENT.getCode(), INSUFFICIENT.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }

        Optional<Account> toAccountOptional = accountRepository.findAccountByIdentifier(toIdentifier.toUpperCase());
        if (!toAccountOptional.isPresent()){
            return new CommonResponse(ACCOUNT_NOT_EXIST.getCode(), ACCOUNT_NOT_EXIST.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }
        if (!toAccountOptional.get().getEnabled()){
            return new CommonResponse(DEACTIVATED.getCode(), DEACTIVATED.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }
        if (toAccountOptional.get().getIdentifier().equals(fromAccountOptional.get().getIdentifier())){
            return new CommonResponse(SAME_ACCOUNT.getCode(), SAME_ACCOUNT.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }

        Account fromAccount = fromAccountOptional.get();
        Account toAccount = toAccountOptional.get();

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transfer transfer = transferRepository.save(new Transfer(fromAccount, toAccount, amount, LocalDateTime.now()));

        return CommonResponse.success(transfer);
    }
}
