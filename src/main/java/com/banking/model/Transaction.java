package com.banking.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="transactions")
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionId;
	
	@Column(nullable = false)
	private String accountNumber;
	
	@NotBlank(message="Transaction type is required")
	@Column(nullable=false)
	private String transactionType;
	
	@DecimalMin(value="0.01", message="Amount must be greater than zero")
	@Column(nullable=false, precision=15, scale=2)
	private BigDecimal amount;
	
	@Column(nullable=false, updatable=false)
	private LocalDateTime transactionTime;
	
	private String description;
	
	@Column(nullable=false)
	private String status = "SUCCESS";
	
	@PrePersist
	protected void onCreate() {
		transactionTime=LocalDateTime.now();
	}
	
	public Transaction() {}
	
	public Transaction(String accountNumber, String transactionType, BigDecimal amount, String description) {
		this.accountNumber=accountNumber;
		this.transactionType=transactionType;
		this.amount=amount;
		this.description=description;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDateTime getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(LocalDateTime transactionTime) {
		this.transactionTime = transactionTime;
	}
	
	@JsonProperty("createdAt")
	public LocalDateTime getCreatedAt() {
	    return this.transactionTime;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
