package com.products.app.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
	
	private String id;
	
	private String name;
	
	//ISO-4217
	private BigDecimal price;
	
	private Boolean availability;

}
