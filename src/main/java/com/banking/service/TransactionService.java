package com.banking.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    // =========================
    // FUND TRANSFER
    // =========================
    public void transferMoney(String fromAccount,
                              String toAccount,
                              BigDecimal amount) {

        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Amount must be greater than zero");
        }

        Account sender = accountRepository.findByAccountNumber(fromAccount)
                .orElseThrow(() ->
                        new RuntimeException("Sender account not found"));

        Account receiver = accountRepository.findByAccountNumber(toAccount)
                .orElseThrow(() ->
                        new RuntimeException("Receiver account not found"));

        if(!sender.getStatus().equals("ACTIVE")){
            throw new RuntimeException("Sender account is not active");
        }

        if(!receiver.getStatus().equals("ACTIVE")){
            throw new RuntimeException("Receiver account is not active");
        }

        if(sender.getBalance().compareTo(amount) < 0){
            throw new RuntimeException("Insufficient balance");
        }

        // Debit sender
        sender.setBalance(sender.getBalance().subtract(amount));
        accountRepository.save(sender);

        // Credit receiver
        receiver.setBalance(receiver.getBalance().add(amount));
        accountRepository.save(receiver);

        // Sender transaction
        Transaction debitTxn = new Transaction(
                fromAccount,
                "TRANSFER_OUT",
                amount,
                "Transferred to " + toAccount
        );

        // Receiver transaction
        Transaction creditTxn = new Transaction(
                toAccount,
                "TRANSFER_IN",
                amount,
                "Received from " + fromAccount
        );

        transactionRepository.save(debitTxn);
        transactionRepository.save(creditTxn);
    }

    // =========================
    // VIEW TRANSACTIONS
    // =========================
    public List<Transaction> getTransactionsByAccountNumber(String accountNumber){
        return transactionRepository
                .findByAccountNumberOrderByTransactionTimeDesc(accountNumber);
    }

    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }
    
    public List<Transaction> getReceivedMoney(String accountNumber){
        return transactionRepository
            .findByAccountNumberAndTransactionTypeOrderByTransactionTimeDesc(
                accountNumber,
                "TRANSFER_IN"
            );
    }

}
