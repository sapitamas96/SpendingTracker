package com.spending.tracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spending.tracker.dto.TransactionDTO;
import com.spending.tracker.model.User;
import com.spending.tracker.service.TransactionService;
import com.spending.tracker.service.UserService;

@RestController
public class Controller {
	@Autowired
	private UserService userService;

	@Autowired
	private TransactionService transactionService;

	@PostMapping("/user/create")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		return ResponseEntity.ok(userService.addUser(user));
	}

	@GetMapping("/user/get")
	public ResponseEntity<User> getUser(@RequestParam String id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@DeleteMapping("/user/delete")
	public ResponseEntity<String> deleteUser(@RequestParam String id) {
		userService.deleteUserById(id);
		return ResponseEntity.ok("User deleted successfully");
	}

	@PostMapping("/transaction/create")
	public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transaction) {
		return ResponseEntity.ok(transactionService.addTransaction(transaction));
	}

	@PutMapping("/transaction/update")
	public ResponseEntity<TransactionDTO> updateTransaction(@RequestBody TransactionDTO transaction) {
		return ResponseEntity.ok(transactionService.updateTransaction(transaction));
	}

	@GetMapping("/transaction/getAll")
	public ResponseEntity<List<TransactionDTO>> getAllTransactions(@RequestParam String userId) {
		return ResponseEntity.ok(transactionService.getUserTransactions(userId));
	}

	@GetMapping("/transaction/getAll/page")
	public ResponseEntity<Page<TransactionDTO>> getAllTransactions(
			@RequestParam String userId, @RequestParam int page, @RequestParam int size) {
		return ResponseEntity.ok(transactionService.getUserTransactions(userId, page, size));
	}

	@DeleteMapping("/transaction/delete")
	public ResponseEntity<String> deleteTransaction(@RequestParam String id) {
		transactionService.deleteTransactionById(id);
		return ResponseEntity.ok("Transaction deleted successfully");
	}

	@GetMapping("/transaction/avgMonthlySpending")
	public ResponseEntity<Double> getAvgMonthlySpending(@RequestParam String userId) {
		return ResponseEntity.ok(transactionService.getAvgMonthlySpendingByUserId(userId));
	}
}
