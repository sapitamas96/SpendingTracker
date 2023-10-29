package com.spending.tracker.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spending.tracker.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
	static final String AVG_MONTHLY_SPENDING = "SELECT SUM(sum) / 2 FROM transactions "
			+ "WHERE user_id = ?1 "
			+ "AND paid_at BETWEEN DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '2 month' "
			+ "AND DATE_TRUNC('month', CURRENT_DATE)";

	List<Transaction> findAllByUserId(UUID userId);
	Page<Transaction> findAllByUserId(UUID userId, Pageable pageable);
	@Query(value = AVG_MONTHLY_SPENDING, nativeQuery = true)
	Double getAvgMonthlySpendingByUserId(UUID userId);
}
