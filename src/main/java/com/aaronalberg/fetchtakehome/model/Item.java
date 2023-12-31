package com.aaronalberg.fetchtakehome.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Item {
	private String shortDescription;
	private double price;
}
