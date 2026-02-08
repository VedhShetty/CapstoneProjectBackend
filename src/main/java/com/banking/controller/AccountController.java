package com.banking.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banking.model.Account;
import com.banking.service.AccountService;
import com.banking.service.RoleValidator;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins="http://localhost:4200")
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private RoleValidator roleValidator;
	
	@PostMapping
	public ResponseEntity<?> createAccount(
			@RequestBody Account account,
			@RequestHeader("userRole") String role){
		try {
			roleValidator.validateAdmin(role);
			Account createdAccount = accountService.createAccount(account);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Account created successfully");
			response.put("account", createdAccount);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}catch(Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	
	@GetMapping
	public ResponseEntity<?> getAllAccounts(@RequestHeader("userRole") String role){
		try {
			roleValidator.validateAdmin(role);
			List<Account> accounts = accountService.getAllAccounts();
			Map<String,Object> response = new HashMap<>();
			response.put("success", true);
			response.put("accounts", accounts);
			response.put("count",accounts.size());
			return ResponseEntity.ok(response);
		}catch(Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
	}
	
	@GetMapping("/{accountNumber}")
	public ResponseEntity<?> getAccountByNumber(
			@PathVariable String accountNumber,
			@RequestHeader("userRole") String role,
			@RequestHeader("userId") Long loggedInUserId){
		try {
			Account account = accountService.getAccountByNumber(accountNumber)
					.orElseThrow(() -> new RuntimeException("Account not found"));
			roleValidator.validateAccountAccess(role,account.getUserId(), loggedInUserId);
			Map<String,Object> response = new HashMap<>();
			response.put("success", true);
			response.put("account", account);
			return ResponseEntity.ok(response);
		}catch(Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getAccountsByUserId(
			@PathVariable Long userId,
			@RequestHeader("userRole") String role,
			@RequestHeader("userId") Long loggedInUserId){
		try {
			roleValidator.validateCustomerAccessById(role, userId, loggedInUserId);
			List<Account> accounts= accountService.getAccountsByUserId(userId);
			Map<String,Object> response = new HashMap<>();
			response.put("success", true);
			response.put("account", accounts);
			response.put("count", accounts.size());
			return ResponseEntity.ok(response);
		}catch(Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
	}
	
	@GetMapping("/status/{status}")
	public ResponseEntity<?> getAccountsByStatus(
			@PathVariable String status,
			@RequestHeader("userRole") String role){
		try {
			roleValidator.validateAdmin(role);
			List<Account> accounts = accountService.getAccountsByStatus(status);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("account", accounts);
			response.put("count", accounts.size());
			return ResponseEntity.ok(response);
		}catch(Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
	}
	
	@GetMapping("/type/{accountType}")
	public ResponseEntity<?>getAccountsByType(
			@PathVariable String accountType,
			@RequestHeader("userRole") String role){
		try {
			roleValidator.validateAdmin(role);
			List<Account> accounts = accountService.getAccountsByType(accountType);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("account", accounts);
			response.put("count", accounts.size());
			return ResponseEntity.ok(response);
		}catch(Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
	}
	
	@PutMapping("/{accountNumber}/status")
	public ResponseEntity<?> updateAccountStatus(
			@PathVariable String accountNumber,
			@RequestBody Map<String, String> statusData,
			@RequestHeader("userRole") String role){
		try {
			roleValidator.validateAdmin(role);
			String status=statusData.get("status");
			Account account = accountService.updateAccountStatus(accountNumber, status);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Account status updated");
			response.put("account", account);
			return ResponseEntity.ok(response);
		}catch(Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	
	@PutMapping("/{acountNumber}/close")
	public ResponseEntity<?> closeAccount(
			@PathVariable String accountNumber,
			@RequestHeader("userRole") String role){
		try {
			roleValidator.validateAdmin(role);
			Account account = accountService.closeAccount(accountNumber);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Account status updated");
			response.put("account", account);
			return ResponseEntity.ok(response);
		}catch(Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	
	@DeleteMapping("/{accountNumber}")
	public ResponseEntity<?> deleteAccount(
			@PathVariable String accountNumber,
			@RequestHeader("userRole") String role){
		try {
			roleValidator.validateAdmin(role);
			accountService.deleteAccount(accountNumber);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Account deleted Successfully");
			return ResponseEntity.ok(response);
		}catch(Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
}
