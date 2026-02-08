package com.banking.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.service.AccountService;
import com.banking.service.RoleValidator;
import com.banking.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleValidator roleValidator;

    // =========================
    // CUSTOMER FUND TRANSFER
    // =========================
    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(
            @RequestBody Map<String,Object> request,
            @RequestHeader("userRole") String role,
            @RequestHeader("userId") Long userId){

        try{
            if(!role.equals("CUSTOMER")){
                throw new RuntimeException("Only customers can transfer money");
            }

            String fromAccount = (String) request.get("fromAccount");
            String toAccount   = (String) request.get("toAccount");
            BigDecimal amount =
                    new BigDecimal(request.get("amount").toString());

            Account sender =
                    accountService.getAccountByNumber(fromAccount)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            // Customer can use only his own account
            roleValidator.validateAccountAccess(
                    role,
                    sender.getUserId(),
                    userId
            );

            transactionService.transferMoney(
                    fromAccount,
                    toAccount,
                    amount
            );

            Map<String,Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Transfer Successful");

            return ResponseEntity.ok(response);

        }catch(Exception e){

            Map<String,Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    // =========================
    // MY TRANSACTIONS
    // =========================
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<?> getTransactionsByAccount(
            @PathVariable String accountNumber,
            @RequestHeader("userRole") String role,
            @RequestHeader("userId") Long userId){

        try{

            Account account =
                    accountService.getAccountByNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            roleValidator.validateAccountAccess(
                    role,
                    account.getUserId(),
                    userId
            );

            List<Transaction> list =
                    transactionService
                    .getTransactionsByAccountNumber(accountNumber);

            Map<String,Object> response = new HashMap<>();
            response.put("success", true);
            response.put("transactions", list);

            return ResponseEntity.ok(response);

        }catch(Exception e){

            Map<String,Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping("/received/{accountNumber}")
    public ResponseEntity<?> getReceivedMoney(
            @PathVariable String accountNumber,
            @RequestHeader("userRole") String role,
            @RequestHeader("userId") Long userId){

        try{

            Account account = accountService
                    .getAccountByNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            roleValidator.validateAccountAccess(
                    role,
                    account.getUserId(),
                    userId
            );

            List<Transaction> list =
                    transactionService.getReceivedMoney(accountNumber);

            Map<String,Object> res = new HashMap<>();
            res.put("success", true);
            res.put("transactions", list);

            return ResponseEntity.ok(res);

        }catch(Exception e){

            Map<String,Object> res = new HashMap<>();
            res.put("success", false);
            res.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(res);
        }
    }
    
 // =========================
 // ADMIN - ALL TRANSACTIONS
 // =========================
 @GetMapping("/admin/all")
 public ResponseEntity<?> getAllTransactionsForAdmin(
         @RequestHeader("userRole") String role) {

     try {
         if (!"ADMIN".equals(role)) {
             throw new RuntimeException("Access denied");
         }

         List<Transaction> list =
                 transactionService.getAllTransactions();

         Map<String, Object> res = new HashMap<>();
         res.put("success", true);
         res.put("transactions", list);

         return ResponseEntity.ok(res);

     } catch (Exception e) {

         Map<String, Object> res = new HashMap<>();
         res.put("success", false);
         res.put("message", e.getMessage());

         return ResponseEntity.badRequest().body(res);
     }
 }


}
