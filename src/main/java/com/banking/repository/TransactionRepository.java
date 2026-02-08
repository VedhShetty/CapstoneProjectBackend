package com.banking.repository;

import com.banking.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long>{
	List<Transaction> findByAccountNumber(String accountNumber);
	List<Transaction> findByAccountNumberOrderByTransactionTimeDesc(String accountNumber);
	List<Transaction> findByTransactionType(String transactionType);
	List<Transaction> findByStatus(String status);
	List<Transaction> findByAccountNumberAndTransactionTimeBetween(
			String accountNumber,
			LocalDateTime startDate,
			LocalDateTime endDate
	);
	List<Transaction> findByTransactionTimeBetween(
			LocalDateTime startDate,
			LocalDateTime endDate
	);
	
	List<Transaction> 
	findByAccountNumberAndTransactionTypeOrderByTransactionTimeDesc(
	        String accountNumber,
	        String transactionType
	);

	
}
