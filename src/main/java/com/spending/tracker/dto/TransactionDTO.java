package com.spending.tracker.dto;

import com.spending.tracker.model.Transaction;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionDTO {
	private String id;
	private String summary;
	private String category;
	private String sum;
	private String currency;
	private String paidAt;
	private String userId;

	public static TransactionDTO from (Transaction transaction) {
		return TransactionDTO.builder()
				.id(transaction.getId().toString())
				.summary(transaction.getSummary())
				.category(transaction.getCategory())
				.sum(String.valueOf(transaction.getSum()))
				.currency(String.valueOf(transaction.getCurrency()))
				.paidAt(String.valueOf(transaction.getPaidAt()))
				.userId(transaction.getUser().getId().toString())
				.build();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof TransactionDTO that))
			return false;

		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return category.hashCode();
	}
}
