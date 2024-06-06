package az.unibank.unitech.services;
import az.unibank.unitech.dto.response.CommonResponse;
import az.unibank.unitech.entities.Account;
import az.unibank.unitech.entities.User;
import az.unibank.unitech.repos.AccountRepository;
import az.unibank.unitech.repos.UserRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static az.unibank.unitech.enums.ResponseEnum.*;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;

    public CommonResponse getUserEnabledAccounts(long userId){
        var accounts = accountRepository.findUserAccounts(userId);
        return CommonResponse.success(accounts);
    }

    public CommonResponse addAccount(User user){

        Optional<Long> lastIdOptional = accountRepository.lastAccountId();
        long lastId = lastIdOptional.orElse(1L);
        lastId++;

        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);
        account.setEnabled(true);
        account.setCreatedAt(LocalDateTime.now());
        account.setUser(user);
        account.setIdentifier(user.getUsername().toUpperCase() + lastId);

        accountRepository.save(account);
        return CommonResponse.success(account);
    }

    public CommonResponse disableAccount(long userId, long id){
        Optional<Account> accountOptional = accountRepository.findAccountByIdAndUser(id, userId);
        if (!accountOptional.isPresent()){
            return new CommonResponse(ACCOUNT_NOT_FOUND.getCode(), ACCOUNT_NOT_FOUND.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }

        Account account = accountOptional.get();
        account.setEnabled(false);

        accountRepository.save(account);
        return CommonResponse.success(account);
    }

    public CommonResponse addBalance(String identifier, BigDecimal amount){
        // THIS METHOD ONLY FOR TESTING. IT MUST DELETE ON PROD!
        if (amount.compareTo(BigDecimal.valueOf(1))<0){
            return new CommonResponse(SMALL_AMOUNT.getCode(), SMALL_AMOUNT.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }

        Optional<Account> accountOptional = accountRepository.findAccountByIdentifier(identifier);
        if (!accountOptional.isPresent()){
            return new CommonResponse(ACCOUNT_NOT_FOUND.getCode(), ACCOUNT_NOT_FOUND.getMessage(), "", new Timestamp(System.currentTimeMillis()).getTime());
        }

        Account account = accountOptional.get();
        account.setBalance(account.getBalance().add(amount));

        accountRepository.save(account);

        return CommonResponse.success(accountRepository.findById(account.getId()));
    }

}
