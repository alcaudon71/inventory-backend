package com.company.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;
import com.company.inventory.services.ICategoryService;

@RestController
@RequestMapping("/api/v1")
public class CategoryRestController {
	
	@Autowired
	private ICategoryService service;
	
	/**
	 * Controlador para invocar al servicio de obtencion de todas las categorias
	 * @return	ResponseEntity	Datos de todas las categorias existentes
	 */
	// endpoint: /api/v1/categories
	@GetMapping("/categories")
	public ResponseEntity<CategoryResponseRest> searchCategories() {
		
		ResponseEntity<CategoryResponseRest> response = service.search();
		
		return response;
		
	}
	
	/**
	 * Controlador para invocar al servicio de obtencion de categorias por Id
	 * @param  id   			ID de la categoria
	 * @return ResponseEntity	Datos de la categoria buscada
	 */
	// endpoint: /api/v1/categories/{Id}
	// @PathVariable ---> recoge el endpoint de entrada en formato "Decoded"
	//                    http://localhost:8080/api/v1/categories/abc
	// @RequestParam ---> recoge el endpoint de entrada en formato "Encoded"
	//  				  http://localhost:8080/api/v1/categories?id=abc
	@GetMapping("/categories/{id}")
	public ResponseEntity<CategoryResponseRest> searchCategoriesById(@PathVariable Long id) {
		
		ResponseEntity<CategoryResponseRest> response = service.searchById(id);
		
		return response;
		
	}
	
	/**
	 * Controlador para invocar al servicio de almacenamiento de categoria
	 * @param 	Category		Categoria a almacenar
	 * @return	ResponseEntity	Datos de la categoria almacenada
	 */
	// endpoint: /api/v1/categories
	// @RequestBody ---> recoge el envio en formato json y lo mapea con el objeto Category 
	@PostMapping("/categories")
	public ResponseEntity<CategoryResponseRest> save (@RequestBody Category category) {
		 
		ResponseEntity<CategoryResponseRest> response = service.save(category);
		
		return response;
		
	}	
	
	/**
	 * Controlador para invocar al servicio de actualizacion de categoria
	 * @param category			Nuevos datos de la categoria
	 * @param id				Identificador de la categoria que debe ser actualizada
	 * @return ResponseEntity	Datos de la categoria modificada
	 */
	// endpoint: /api/v1/categories
	@PutMapping("/categories/{id}")
	public ResponseEntity<CategoryResponseRest> update (@RequestBody Category category, @PathVariable Long id) {
		 
		ResponseEntity<CategoryResponseRest> response = service.update(category, id);
		
		return response;
		
	}	
	
	/**
	 * Controlador para invocar al servicio de eliminacion de categoria
	 * @param id				Identificador de la categoria a eliminar
	 * @return ResponseEntity	Datos de la categoria eliminada
	 */
	// endpoint: /api/v1/categories
	@DeleteMapping("/categories/{id}")
	public ResponseEntity<CategoryResponseRest> delete (@PathVariable Long id) {
		 
		ResponseEntity<CategoryResponseRest> response = service.deleteById(id);
		
		return response;
		
	}	
	
}
