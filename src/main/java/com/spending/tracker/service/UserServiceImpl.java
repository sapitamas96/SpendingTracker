package com.spending.tracker.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spending.tracker.exception.BusinessException;
import com.spending.tracker.model.User;
import com.spending.tracker.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User addUser(User user) {
		String email = user.getEmail();

		if (userExists(email)) {
			throw new BusinessException("User with email " + email + " already exists");
		}

		return userRepository.save(new User(user.getFirstName(), user.getLastName(), email));
	}

	private boolean userExists(String email) {
		return userRepository.findByEmail(email).isPresent();
	}

	@Override
	public User getUserById(String id) {
		return userRepository.findById(
				UUID.fromString(id)).orElseThrow(() -> new BusinessException("User not found with id: " + id));
	}

	@Override
	public void deleteUserById(String id) {
		// q: for this repo method how can I delete all the transactions associated with this user?
		userRepository.deleteById(UUID.fromString(id));
	}
}
