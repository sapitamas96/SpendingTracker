package com.spending.tracker.model;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

import com.spending.tracker.dto.TransactionDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;

	@Column(name = "summary")
	private String summary;

	@Column(name = "category")
	private String category;

	@Column(name = "sum")
	private BigDecimal sum;

	@Column(name = "currency")
	private Currency currency;

	@Column(name = "paid_at")
	private LocalDateTime paidAt;

	@ManyToOne(cascade = { CascadeType.ALL })
	private User user;

	public static Transaction from(TransactionDTO transactionDTO) {
		Transaction transaction = new Transaction();
		transaction.setId(UUID.fromString(transactionDTO.getId()));
		transaction.setSummary(transactionDTO.getSummary());
		transaction.setCategory(transactionDTO.getCategory());
		transaction.setSum(new BigDecimal(transactionDTO.getSum()));
		transaction.setCurrency(Currency.getInstance(transactionDTO.getCurrency()));
		transaction.setPaidAt(LocalDateTime.parse(transactionDTO.getPaidAt()));
		return transaction;
	}
}
