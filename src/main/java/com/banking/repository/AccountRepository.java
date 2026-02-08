package com.banking.repository;

import com.banking.model.Account;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String>{
	List<Account> findByUserId(Long userId);
	Optional<Account> findByAccountNumber(String accountNumber);
	List<Account> findByStatus(String status);
	List<Account> findByAccountType(String accountType);
	boolean existsByAccountNumber(String accountNumber);
}
