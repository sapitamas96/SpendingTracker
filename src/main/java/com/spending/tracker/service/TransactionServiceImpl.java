package com.spending.tracker.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.spending.tracker.dto.TransactionDTO;
import com.spending.tracker.exception.BusinessException;
import com.spending.tracker.model.Transaction;
import com.spending.tracker.model.User;
import com.spending.tracker.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private UserService userService;

	@Override
	public TransactionDTO addTransaction(TransactionDTO transactionDTO) {
		Transaction transaction = Transaction.from(transactionDTO);
		transaction.setUser(getUserById(transactionDTO.getUserId()));
		return TransactionDTO.from(transactionRepository.save(transaction));
	}

	private User getUserById(String id) {
		User user = userService.getUserById(id);
		if (user == null) {
			throw new BusinessException("User not found with id: " + id);
		}

		return user;
	}

	@Override
	public TransactionDTO updateTransaction(TransactionDTO transactionDTO) {
		Transaction transaction = transactionRepository.findById(UUID.fromString(transactionDTO.getId()))
				.orElseThrow(() -> new BusinessException("Transaction not found with id: " + transactionDTO.getId()));
		transaction.setSummary(transactionDTO.getSummary());
		transaction.setCategory(transactionDTO.getCategory());
		transaction.setSum(new BigDecimal(transactionDTO.getSum()));
		transaction.setCurrency(Currency.getInstance(transactionDTO.getCurrency()));
		transaction.setPaidAt(LocalDateTime.parse(transactionDTO.getPaidAt()));
		transactionRepository.save(transaction);
		return transactionDTO;
	}

	@Override
	public List<TransactionDTO> getUserTransactions(String userId) {
		return transactionRepository.findAllByUserId(UUID.fromString(userId))
				.stream()
				.map(TransactionDTO::from)
				.toList();
	}

	@Override
	public Page<TransactionDTO> getUserTransactions(String userId, int page, int size) {
		Page<Transaction> transactionPage = transactionRepository.findAllByUserId(
				UUID.fromString(userId), PageRequest.of(page, size));
		List<TransactionDTO> transactionDTOs = transactionPage
				.stream()
				.map(TransactionDTO::from)
				.toList();
		return new PageImpl<>(transactionDTOs, transactionPage.getPageable(), transactionPage.getTotalElements());
	}

	@Override
	public void deleteTransactionById(String id) {
		transactionRepository.deleteById(UUID.fromString(id));
	}

	@Override
	public Double getAvgMonthlySpendingByUserId(String userId) {
		return transactionRepository.getAvgMonthlySpendingByUserId(UUID.fromString(userId));
	}
}
