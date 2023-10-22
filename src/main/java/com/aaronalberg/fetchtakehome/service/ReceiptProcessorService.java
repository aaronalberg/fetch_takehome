package com.aaronalberg.fetchtakehome.service;

import com.aaronalberg.fetchtakehome.model.Item;
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

		Receipt receiptEntity = Receipt.builder()
			.id(UUID.randomUUID().toString())
			.retailer(input.getRetailer())
			.purchaseDate(LocalDate.parse(input.getPurchaseDate()))
			.purchaseTime(LocalTime.parse(input.getPurchaseTime()))
			.total(parseDouble(input.getTotal()))
			.items(input.getItems())
			.build();

		int pointsAwarded = awardPoints(receiptEntity);

		receiptEntity.setPointsAwarded(pointsAwarded);


		receiptDB.addReceipt(receiptEntity);

		return receiptEntity.getId();
	}

	private int awardPoints(Receipt receipt) {
		int grandTotal = 0;

		// One point for every alphanumeric character in the retailer name.
		int alphanumericCount = 0;
		for (char c : receipt.getRetailer().toCharArray()) {
			if (Character.isLetterOrDigit(c)) {
				alphanumericCount++;
			}
		}
		grandTotal += alphanumericCount;

		// 50 points if the total is a round dollar amount with no cents.
		if (receipt.getTotal() % 1 == 0) {
			grandTotal += 50;
		}

		// 25 points if the total is a multiple of 0.25.
		if (receipt.getTotal() % .25 == 0) {
			grandTotal += 25;
		}

		// 5 points for every two items on the receipt.
		int itemSizeQuotient = receipt.getItems().size() / 2;
		grandTotal += (itemSizeQuotient * 5);

		// If the trimmed length of the item description is a multiple of 3,
		// multiply the price by 0.2 and round up to the nearest integer.
		// The result is the number of points earned.
		for (Item item : receipt.getItems()) {
			if (item.getShortDescription().trim().length() % 3 == 0) {
				double price = parseDouble(item.getPrice());
				grandTotal += (int) Math.ceil(price * .2);
			}
		}

		// 6 points if the day in the purchase date is odd.
		if (receipt.getPurchaseDate().getDayOfMonth() % 2 == 1) {
			grandTotal += 6;
		}

		// 10 points if the time of purchase is after 2:00pm and before 4:00pm.
		if (receipt.getPurchaseTime().isAfter(LocalTime.parse("14:00")) &&
		receipt.getPurchaseTime().isBefore(LocalTime.parse("16:00"))) {
			grandTotal += 10;
		}


		return grandTotal;
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

		if (input.getItems().isEmpty()) {
			throw new IllegalArgumentException("no items");
		}
	}
}
