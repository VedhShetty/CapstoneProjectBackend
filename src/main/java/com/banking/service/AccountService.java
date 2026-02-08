package com.banking.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.model.Account;
import com.banking.repository.AccountRepository;
import com.banking.repository.UserRepository;

@Service
@Transactional
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    // Generate unique account number
    private String generateAccountNumber() {
        Random random = new Random();
        String accountNumber;

        do {
            accountNumber = "ACC" +
                    String.format("%010d",
                    Math.abs(random.nextLong() % 10000000000L));
        } while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }

    // CREATE ACCOUNT WITH BUSINESS RULES
    public Account createAccount(Account account) {

        // 1️⃣ Check customer exists
        if(!userRepository.existsById(account.getUserId())){
            throw new RuntimeException("Customer does not exist");
        }

        // 2️⃣ Fetch existing accounts of this customer
        List<Account> existingAccounts =
                accountRepository.findByUserId(account.getUserId());

        // 3️⃣ Maximum 2 accounts per customer
        if(existingAccounts.size() >= 2){
            throw new RuntimeException(
                    "Customer already has maximum 2 accounts");
        }

        // 4️⃣ Only one account per type
        boolean sameTypeExists = existingAccounts.stream()
                .anyMatch(a ->
                        a.getAccountType()
                         .equalsIgnoreCase(account.getAccountType())
                );

        if(sameTypeExists){
            throw new RuntimeException(
                    "Customer already has this account type");
        }

        // 5️⃣ Generate account number
        String accountNumber = generateAccountNumber();
        account.setAccountNumber(accountNumber);

        // 6️⃣ Default balance
        if(account.getBalance() == null){
            account.setBalance(BigDecimal.ZERO);
        }

        if(account.getBalance().compareTo(BigDecimal.ZERO) < 0){
            throw new RuntimeException(
                    "Initial Balance cannot be negative");
        }

        // 7️⃣ Default status
        if(account.getStatus() == null || account.getStatus().isEmpty()){
            account.setStatus("ACTIVE");
        }

        // 8️⃣ Default currency
        if(account.getCurrency() == null || account.getCurrency().isEmpty()){
            account.setCurrency("INR");
        }

        return accountRepository.save(account);
    }

    // ===============================

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountByNumber(String accountNumber){
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public List<Account> getAccountsByUserId(Long userId){
        return accountRepository.findByUserId(userId);
    }

    public List<Account> getAccountsByStatus(String status){
        return accountRepository.findByStatus(status);
    }

    public List<Account> getAccountsByType(String accountType){
        return accountRepository.findByAccountType(accountType);
    }

    public Account updateAccountStatus(String accountNumber, String status) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException(
                        "Account not found with number: " + accountNumber));

        account.setStatus(status);
        return accountRepository.save(account);
    }

    public Account updateBalance(String accountNumber,
                                 BigDecimal newBalance) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException(
                        "Account not found with number: " + accountNumber));

        if(newBalance.compareTo(BigDecimal.ZERO) < 0){
            throw new RuntimeException(
                    "Balance cannot be negative");
        }

        account.setBalance(newBalance);
        return accountRepository.save(account);
    }

    public Account closeAccount(String accountNumber) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException(
                        "Account not found with number: " + accountNumber));

        if(account.getBalance().compareTo(BigDecimal.ZERO) != 0){
            throw new RuntimeException(
                    "Cannot close account with non zero balance. " +
                    "Current balance: " + account.getBalance());
        }

        account.setStatus("CLOSED");
        return accountRepository.save(account);
    }

    public void deleteAccount(String accountNumber) {

        if(!accountRepository.existsByAccountNumber(accountNumber)){
            throw new RuntimeException(
                    "Account not found with number: " + accountNumber);
        }

        accountRepository.deleteById(accountNumber);
    }

    public boolean accountExists(String accountNumber){
        return accountRepository.existsByAccountNumber(accountNumber);
    }
}
