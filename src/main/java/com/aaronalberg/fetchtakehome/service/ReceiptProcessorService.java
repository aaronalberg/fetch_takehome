package com.aaronalberg.fetchtakehome.service;

import com.aaronalberg.fetchtakehome.model.Receipt;
import com.aaronalberg.fetchtakehome.model.ReceiptDB;
import com.aaronalberg.fetchtakehome.model.ReceiptInput;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static java.lang.Double.parseDouble;

@Service
@AllArgsConstructor
public class ReceiptProcessorService {

	private final ReceiptDB receiptDB;
	/**
	 * @param input receipt from POST request
	 * @return id of receipt if processed successfully
	 */
	public String processReceipt(ReceiptInput input) {
		validateInputPresent(input);
		int pointsAwarded = awardPoints(input);

		// create receipt entity in DB
		Receipt receiptEntity = Receipt.builder()
			.id(UUID.randomUUID().toString())
			.retailer(input.getRetailer())
			.purchaseDate(LocalDate.parse(input.getPurchaseDate()))
			.purchaseTime(LocalTime.parse(input.getPurchaseTime()))
			.total(parseDouble(input.getTotal()))
			.itemInputs(input.getItemInputs())
			.pointsAwarded(pointsAwarded)
			.build();



		receiptDB.addReceipt(receiptEntity);

		return receiptEntity.getId();
	}

	private int awardPoints(ReceiptInput input) {
		// TODO update logic
		return 3058;
	}

	private void validateInputPresent(ReceiptInput input) {

		boolean allNull = Arrays.stream(input.getClass()
				.getDeclaredFields())
			.peek(f -> f.setAccessible(true))
			.map(f -> {
				try {
					return f.get(input);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			})
			.allMatch(Objects::isNull);

		if (allNull) {
			throw new IllegalArgumentException("not all fields present");
		}

		if (input.getItemInputs().isEmpty()) {
			throw new IllegalArgumentException("no total");
		}
	}
}
