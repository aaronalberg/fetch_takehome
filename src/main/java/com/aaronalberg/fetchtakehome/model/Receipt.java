package com.aaronalberg.fetchtakehome.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
public class Receipt {
	private String id;
	private String retailer;
	private LocalDate purchaseDate;
	private LocalTime purchaseTime;
	private double total;
	private List<Item> items;
	private int pointsAwarded;

}
