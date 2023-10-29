package com.spending.tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.spending.tracker.dto.TransactionDTO;
import com.spending.tracker.model.User;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	private static final User USER1 = new User("John", "Doe", "john.doe@fakemail.com");
	private static final User USER2 = new User("Jane", "Doe", "jane.doe@test.com");
	private static final String INCORRECT_USER_ID = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12";

	private static final TransactionDTO TRANSACTION = TransactionDTO.builder()
			.id(UUID.randomUUID().toString())
			.summary("Groceries")
			.category("Food")
			.sum("100.00")
			.currency("USD")
			.paidAt(LocalDateTime.now().toString())
			.build();

	@BeforeEach
	public void setUp() {
		USER1.setId(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"));
		TRANSACTION.setUserId(USER1.getId().toString());
	}


	@Test
	@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testCreateUser() {
		ResponseEntity<User> response = restTemplate.postForEntity("/user/create", USER2, User.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(USER2, response.getBody());
	}

	@Test
	@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testCreateUserWithExistingEmail() {
		ResponseEntity<String> response = restTemplate.postForEntity("/user/create", USER1, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("User with email " + USER1.getEmail() + " already exists", response.getBody());
	}

	@Test
	@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testGetUser() {
		ResponseEntity<User> response = restTemplate.getForEntity("/user/get?id=" + USER1.getId().toString(), User.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(USER1, response.getBody());
	}

	@Test
	@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testGetUserWithInvalidId() {
		ResponseEntity<String> response = restTemplate.getForEntity("/user/get?id=" + INCORRECT_USER_ID, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("User not found with id: " + INCORRECT_USER_ID, response.getBody());
	}

	@Test
	@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testDeleteUser() {
		ResponseEntity<String> response = restTemplate.exchange("/user/delete?id=" + USER1.getId().toString(),
				org.springframework.http.HttpMethod.DELETE, null, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("User deleted successfully", response.getBody());
	}

	@Test
	@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testCreateTransaction() {
		ResponseEntity<TransactionDTO> response = restTemplate.postForEntity("/transaction/create", TRANSACTION, TransactionDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(TRANSACTION.getSummary(), Objects.requireNonNull(response.getBody()).getSummary());
		assertEquals(TRANSACTION.getCategory(), response.getBody().getCategory());
		assertEquals(TRANSACTION.getSum(), response.getBody().getSum());
		assertEquals(TRANSACTION.getCurrency(), response.getBody().getCurrency());
		assertEquals(TRANSACTION.getPaidAt(), response.getBody().getPaidAt());
	}

	@Test
	@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testCreateTransactionWithInvalidUserId() {
		TRANSACTION.setUserId(INCORRECT_USER_ID);
		ResponseEntity<String> response = restTemplate.postForEntity("/transaction/create", TRANSACTION, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("User not found with id: " + INCORRECT_USER_ID, response.getBody());
	}

	@Test
	@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testUpdateTransaction() {
		ResponseEntity<TransactionDTO> newTransaction = restTemplate.postForEntity(
				"/transaction/create", TRANSACTION, TransactionDTO.class);
		TRANSACTION.setId(Objects.requireNonNull(newTransaction.getBody()).getId());
		TRANSACTION.setSummary("New summary");
		TRANSACTION.setCategory("New category");
		TRANSACTION.setSum("200.00");
		TRANSACTION.setCurrency("EUR");
		TRANSACTION.setPaidAt(LocalDateTime.now().toString());

		ResponseEntity<TransactionDTO> response = restTemplate.exchange(
				"/transaction/update", HttpMethod.PUT, new HttpEntity<>(TRANSACTION), TransactionDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(TRANSACTION.getSummary(), Objects.requireNonNull(response.getBody()).getSummary());
		assertEquals(TRANSACTION.getCategory(), response.getBody().getCategory());
		assertEquals(TRANSACTION.getSum(), response.getBody().getSum());
		assertEquals(TRANSACTION.getCurrency(), response.getBody().getCurrency());
		assertEquals(TRANSACTION.getPaidAt(), response.getBody().getPaidAt());
	}

	@Test
	@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testUpdateTransactionWithInvalidId() {
		TRANSACTION.setId(INCORRECT_USER_ID);
		ResponseEntity<String> response = restTemplate.exchange(
				"/transaction/update", HttpMethod.PUT, new HttpEntity<>(TRANSACTION), String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Transaction not found with id: " + INCORRECT_USER_ID, response.getBody());
	}

	@Test
	@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testGetAllTransactions() {
		ResponseEntity<List<TransactionDTO>> response = restTemplate.exchange(
				"/transaction/getAll?userId=" + USER1.getId().toString(),
				HttpMethod.GET, null, new ParameterizedTypeReference<List<TransactionDTO>>() {});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(9, Objects.requireNonNull(response.getBody()).size());

		assertEquals("Lunch", response.getBody().get(0).getSummary());
		assertEquals("Food", response.getBody().get(0).getCategory());
		assertEquals("10.00", response.getBody().get(0).getSum());
		assertEquals("EUR", response.getBody().get(0).getCurrency());
		assertEquals("2023-08-01T12:00", response.getBody().get(0).getPaidAt());

		assertEquals("Hotel", response.getBody().get(8).getSummary());
		assertEquals("Accommodation", response.getBody().get(8).getCategory());
		assertEquals("100.00", response.getBody().get(8).getSum());
		assertEquals("EUR", response.getBody().get(8).getCurrency());
		assertEquals("2023-09-02T21:00", response.getBody().get(8).getPaidAt());
	}

	@Test
	@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testGetAllTransactionsPaginated() {
		ResponseEntity<Page<Map<String, String>>> response = restTemplate.exchange(
				"/transaction/getAll/page?userId=" + USER1.getId().toString() + "&page=0&size=5",
				HttpMethod.GET, null, new ParameterizedTypeReference<Page<Map<String, String>>>() {});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(9, Objects.requireNonNull(response.getBody()).getTotalElements());

		List<Map<String, String>> results = response.getBody().getContent();

		assertEquals("Lunch", results.get(0).get("summary"));
		assertEquals("Food", results.get(0).get("category"));
		assertEquals("10.00", results.get(0).get("sum"));
		assertEquals("EUR", results.get(0).get("currency"));
		assertEquals("2023-08-01T12:00", results.get(0).get("paidAt"));

		assertEquals("Breakfast", results.get(4).get("summary"));
		assertEquals("Food", results.get(4).get("category"));
		assertEquals("10.00", results.get(4).get("sum"));
		assertEquals("EUR", results.get(4).get("currency"));
		assertEquals("2023-08-02T08:00", results.get(4).get("paidAt"));
	}

	@Test
	@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testDeleteTransaction() {
		ResponseEntity<String> response = restTemplate.exchange(
				"/transaction/delete?id=" + TRANSACTION.getId(),
				HttpMethod.DELETE, null, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Transaction deleted successfully", response.getBody());
	}
}
