package com.spending.tracker.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.spending.tracker.dto.TransactionDTO;

public interface TransactionService {

	TransactionDTO addTransaction(TransactionDTO transactionDTO);
	TransactionDTO updateTransaction(TransactionDTO transactionDTO);
	List<TransactionDTO> getUserTransactions(String userId);
	Page<TransactionDTO> getUserTransactions(String userId, int page, int size);
	void deleteTransactionById(String id);
	Double getAvgMonthlySpendingByUserId(String userId);
}
