package com.aaronalberg.fetchtakehome.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ReceiptInput {
	private String retailer;
	private String purchaseDate;
	private String purchaseTime;
	private String total;
	private List<Item> items;

}
