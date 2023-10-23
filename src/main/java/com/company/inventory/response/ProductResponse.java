package com.company.inventory.response;

import java.util.List;

import com.company.inventory.model.Product;

import lombok.Data;

@Data   // Lombok
public class ProductResponse {

	List<Product> products;
	
}
