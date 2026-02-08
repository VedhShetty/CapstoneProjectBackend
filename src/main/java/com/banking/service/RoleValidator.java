package com.banking.service;

import org.springframework.stereotype.Component;

@Component
public class RoleValidator {
	
	public void validateAdmin(String role) {
		if (!"ADMIN".equals(role)) {
			throw new RuntimeException("Access denied. Admin privileges required.");
		}
	}
	
	public void validateCustomerAccess(String role, Long requestedUsername, Long loggedInUsername) {
		if ("ADMIN".equals(role)) {
			return;
		}
		if ("CUSTOMER".equals(role) && !requestedUsername.equals(loggedInUsername)) {
			throw new RuntimeException("Access denied. You can only access your own data.");
		}
	}
	
	public void validateCustomerAccessById(String role, Long requestedUserId, Long loggedInUserId) {
		if("ADMIN".equals(role)) {
			return;
		}
		if("CUSTOMER".equals(role) && !requestedUserId.equals(loggedInUserId)) {
			throw new RuntimeException("Access denied. You can only access your own data.");
		}
	}
	
	public void validateAccountAccess(String role, Long accountOwnerId, Long loggedInUserId) {
		if ("ADMIN".equals(role)) {
			return;
		}
		
		if ("CUSTOMER".equals(role) && !accountOwnerId.equals(loggedInUserId)) {
			throw new RuntimeException("Access denied. You can only access your own accounts.");
		}
	}
}
