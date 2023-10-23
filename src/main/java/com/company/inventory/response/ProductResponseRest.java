package com.company.inventory.response;

import lombok.Getter;
import lombok.Setter;

@Getter   // Lombok
@Setter 
public class ProductResponseRest extends ResponseRest {

	private ProductResponse productResponse = new ProductResponse();
	
}
