package com.banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name="accounts")
public class Account {
	
	@Id
	@Column(unique=true, nullable=false)
	private String accountNumber;
	
	@Column(nullable=false)
	private Long userId;
	
	@NotBlank(message = "Account type is required")
	@Column(nullable=false)
	private String accountType;
	
	@DecimalMin(value="0.0", inclusive=true, message="Balance cannot be negative")
	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal balance = BigDecimal.ZERO;
	
	@Column(nullable=false)
	private String currency = "INR";
	
	@Column(nullable=false, updatable=false)
	private LocalDateTime openedAt;
	
	@Column(nullable=false)
	private String status="ACTIVE";
	
	@PrePersist
	protected void onCreate() {
		openedAt=LocalDateTime.now();
	}
	public Account() {}
	
	public Account(String accountNumber, Long userId, String accountType, BigDecimal balance) {
		this.accountNumber=accountNumber;
		this.userId=userId;
		this.accountType=accountType;
		this.balance=balance;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public LocalDateTime getOpenedAt() {
		return openedAt;
	}
	public void setOpenedAt(LocalDateTime openedAt) {
		this.openedAt = openedAt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
