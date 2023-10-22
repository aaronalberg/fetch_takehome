package com.aaronalberg.fetchtakehome.controller;


import com.aaronalberg.fetchtakehome.model.*;
import com.aaronalberg.fetchtakehome.service.ReceiptProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReceiptController {
	private final ReceiptProcessorService receiptProcessorService;
	private final ReceiptDB receiptDB;

	@PostMapping("/receipts/process")
	ResponseEntity<?> processReceipt(@RequestBody ReceiptInput receiptToProcess) {
		try {
			String id = receiptProcessorService.processReceipt(receiptToProcess);
			ProcessReceiptOutput output = ProcessReceiptOutput.builder().id(id).build();
			return ResponseEntity.ok(output);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The receipt is invalid: " + e.getLocalizedMessage());
		}
	}

	@GetMapping("/receipts/{id}/points")
	ResponseEntity<?> getPoints(@PathVariable String id) {
		Receipt receipt = receiptDB.getReceiptById(id);
		if (receipt == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No receipt found for that id");
		}

		int pointsAwarded = receipt.getPointsAwarded();
		return ResponseEntity.ok(new GetPointsOutput(String.valueOf(pointsAwarded)));
	}

}
