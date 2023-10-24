package com.company.inventory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.company.inventory.model.Product;

public interface IProductDao extends CrudRepository<Product, Long> {

	// Buscar productos cuyo nombre contengan un determinado string
	@Query("select p from Product p where p.name like %?1 ")
	List<Product> findByNameLike (String cadena);
	
	// Buscar todos los productos cuyo nombre contenga la cadena enviada
	// Metodo en pseudocodigo equivalente al anterior 
	// Docs ---> https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
	List<Product> findByNameContainingIgnoreCase (String cadena);
	
}
