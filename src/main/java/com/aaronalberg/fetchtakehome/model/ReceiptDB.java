package com.aaronalberg.fetchtakehome.model;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReceiptDB {
	private final Map<String, Receipt> allReceipts = new HashMap<>();

	public void addReceipt(Receipt toAdd) {
		if (allReceipts.get(toAdd.getId()) != null) {
			throw new IllegalArgumentException("Already present");
		}

		allReceipts.put(toAdd.getId(), toAdd);
	}

	public Receipt getReceiptById(String id) {
        return allReceipts.get(id);
	}

	public int size() {
		return allReceipts.size();
	}

	public void reset() {
		allReceipts.clear();
	}
}
