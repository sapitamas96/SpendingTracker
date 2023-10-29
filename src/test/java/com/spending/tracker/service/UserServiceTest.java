package com.spending.tracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.spending.tracker.exception.BusinessException;
import com.spending.tracker.model.User;
import com.spending.tracker.repository.UserRepository;

@SpringBootTest
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImpl userService;

	@Test
	public void testAddUser() {
		User user = new User("John", "Doe", "johndoe@example.com");
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
		when(userRepository.save(any(User.class))).thenReturn(user);

		User result = userService.addUser(user);

		assertEquals(user, result);
	}

	@Test
	public void testAddUserWithExistingEmail() {
		User user = new User("John", "Doe", "johndoe@example.com");
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

		BusinessException exception = assertThrows(BusinessException.class, () -> {
			userService.addUser(user);
		});

		assertEquals("User with email johndoe@example.com already exists", exception.getMessage());
	}

	@Test
	public void testGetUserById() {
		UUID id = UUID.randomUUID();
		User user = new User("John", "Doe", "johndoe@example.com");
		user.setId(id);
		when(userRepository.findById(id)).thenReturn(Optional.of(user));

		User result = userService.getUserById(id.toString());

		assertEquals(user, result);
	}

	@Test
	public void testGetUserByIdWithInvalidId() {
		String invalidId = UUID.randomUUID().toString();
		BusinessException exception = assertThrows(BusinessException.class, () -> {
			userService.getUserById(invalidId);
		});

		assertEquals("User not found with id: " + invalidId, exception.getMessage());
	}

	@Test
	public void testDeleteUserById() {
		UUID id = UUID.randomUUID();
		userService.deleteUserById(id.toString());

		verify(userRepository).deleteById(id);
	}
}
