package com.spending.tracker.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.spending.tracker.dto.TransactionDTO;
import com.spending.tracker.exception.BusinessException;
import com.spending.tracker.model.Transaction;
import com.spending.tracker.model.User;
import com.spending.tracker.repository.TransactionRepository;

@SpringBootTest
public class TransactionServiceTest {

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private UserService userService;

	@InjectMocks
	private TransactionServiceImpl transactionService;

	private static User user;
	private static Transaction transaction;
	private static TransactionDTO transactionDTO;


	@BeforeAll
	public static void setUp() {
		user = new User();
		user.setId(UUID.randomUUID());
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setEmail("john.doe@example.com");
		transactionDTO = TransactionDTO.builder()
				.id(UUID.randomUUID().toString())
				.userId(user.getId().toString())
				.summary("Groceries")
				.category("Food")
				.sum("100.00")
				.currency("USD")
				.paidAt(LocalDateTime.now().toString())
				.build();
		transaction = Transaction.from(transactionDTO);
		transaction.setUser(user);
	}

	@Test
	public void testAddTransaction() {
		when(userService.getUserById(user.getId().toString())).thenReturn(user);
		when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

		TransactionDTO result = transactionService.addTransaction(transactionDTO);

		assertEquals(transactionDTO, result);
	}

	@Test
	public void testAddTransactionWithInvalidUser() {
		when(userService.getUserById(any())).thenReturn(null);

		assertThrows(BusinessException.class, () -> transactionService.addTransaction(transactionDTO),
				"User not found with id: " + transactionDTO.getUserId());
	}

	@Test
	public void testUpdateTransaction() {
		when(transactionRepository.findById(UUID.fromString(transactionDTO.getId())))
				.thenReturn(Optional.of(Transaction.from(transactionDTO)));
		when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

		TransactionDTO result = transactionService.updateTransaction(transactionDTO);

		assertEquals(transactionDTO, result);
	}

	@Test
	public void testUpdateTransactionWithInvalidId() {
		when(transactionRepository.findById(any())).thenReturn(Optional.empty());

		assertThrows(BusinessException.class, () -> transactionService.updateTransaction(transactionDTO),
				"Transaction not found with id: " + transactionDTO.getId());
	}

	@Test
	public void testGetUserTransactions() {
		List<Transaction> transactions = List.of(transaction);

		when(transactionRepository.findAllByUserId(user.getId())).thenReturn(transactions);
		List<TransactionDTO> result = transactionService.getUserTransactions(user.getId().toString());

		assertEquals(1, result.size());
		assertEquals(transactionDTO, result.get(0));
	}

	@Test
	public void testGetUserTransactionsWithPagination() {
		List<Transaction> transactions = List.of(transaction);
		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<Transaction> page = new PageImpl<>(transactions, pageRequest, 1);

		when(transactionRepository.findAllByUserId(user.getId(), pageRequest)).thenReturn(page);
		Page<TransactionDTO> result = transactionService.getUserTransactions(user.getId().toString(), 0, 10);


		assertEquals(1, result.getTotalElements());
		assertEquals(1, result.getContent().size());
		assertEquals(transactionDTO, result.getContent().get(0));
	}

	@Test
	public void testDeleteTransactionById() {
		transactionService.deleteTransactionById(transactionDTO.getId());

		verify(transactionRepository).deleteById(UUID.fromString(transactionDTO.getId()));
	}

	@Test
	public void testGetAvgMonthlySpendingByUserId() {
		when(transactionRepository.getAvgMonthlySpendingByUserId(user.getId())).thenReturn(100.00);
		Double result = transactionService.getAvgMonthlySpendingByUserId(user.getId().toString());

		assertEquals(100.00, result);
	}
}

