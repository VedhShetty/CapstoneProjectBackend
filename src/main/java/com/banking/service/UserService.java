package com.banking.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.model.User;
import com.banking.repository.UserRepository;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public User createUser(User user) {
		user.setRole(user.getRole().toUpperCase());
		if (userRepository.existsByUsername(user.getUsername())) {
		    throw new RuntimeException("Username already exists");
		}
		if (userRepository.existsByUsername(user.getUsername())) {
			throw new RuntimeException("Username Already exists");
		}
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new RuntimeException("Email already Exists");
		}
		if (user.getStatus()==null || user.getStatus().isEmpty()) {
			user.setStatus("ACTIVE");
		}
		return userRepository.save(user);
	}
	
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
	
	public Optional<User> getUserById(Long userId){
		return userRepository.findById(userId);
	}
	
	public Optional<User> getUserByUsername(String username){
		return userRepository.findByUsername(username);
	}
	
	public Optional<User> getUserByEmail(String email){
		return userRepository.findByEmail(email);
	}
	
	public List<User> getUsersByRole(String role){
		return userRepository.findByRole(role);
	}
	
	public List<User> getUsersByStatus(String status){
		return userRepository.findByStatus(status);
	}
	
	public User updateUser(long userId, User userDetails) {
		User user = userRepository.findById(userId)
				.orElseThrow(()->new RuntimeException("User not found with id: "+userId));
		user.setFirstName(userDetails.getFirstName());
		user.setLastName(userDetails.getLastName());
		user.setEmail(userDetails.getEmail());
		user.setPhone(userDetails.getPhone());
		user.setAddressLine1(userDetails.getAddressLine1());
		user.setAddressLine2(userDetails.getAddressLine2());
		user.setCity(userDetails.getCity());
		user.setState(userDetails.getState());
		user.setPostalCode(userDetails.getPostalCode());
		
		if (userDetails.getStatus()!=null && !userDetails.getStatus().isEmpty()) {
			user.setStatus(userDetails.getStatus());
		}
		return userRepository.save(user);
	}
	
	public User updatePassword(Long userId, String newPassword) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with id: "+userId));
		
		user.setPasswordHash(newPassword);
		return userRepository.save(user);
	}
	
	public User updateUserStatus(Long userId, String status) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with id: "+userId));
		user.setStatus(status);
		return userRepository.save(user);
	}
	
	public void deleteUser(Long userId) {
		if (!userRepository.existsById(userId)) {
			throw new RuntimeException("User not found with id: "+userId);
		}
		userRepository.deleteById(userId);
	}
	
	public User login(String username, String password) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Invalid username or password"));
		if (!user.getPasswordHash().equals(password)) {
			throw new RuntimeException("Inavalid username or password");
		}
		if(!"ACTIVE".equals(user.getStatus())) {
			throw new RuntimeException("Account is "+user.getStatus().toLowerCase()+". Please contact administrator.");
		}
		return user;
	}
	
	public boolean usernameExists(String username) {
		return userRepository.existsByUsername(username);
	}
	
	public boolean emailExists(String email) {
		return userRepository.existsByEmail(email);
	}
}
