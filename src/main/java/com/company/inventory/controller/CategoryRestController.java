package com.company.inventory.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.company.inventory.util.CategoryExcelExporter;

import jakarta.servlet.http.HttpServletResponse;

// CrossOrigin ---> Permite el acceso desde una aplicacion FrontEnd externa a nuestro controlador BackEnd
// Habilitamos el acceso desde localhost:4200, que es el puerto por defecto utilizado por los FrontEnd de Angular
@CrossOrigin(origins = {"http://localhost:4200"})
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
		
		//System.out.println(${app.inicio]);
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
	
	/**
	 * Controlador para invocar al servicio de Almacenar categorias en fichero excel
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/categories/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		
		// Indicamos el tipo de contenido
		response.setContentType("application/octet-stream");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=result_category.xlsx";  // nombre del archivo excel xml (posterior a 2007)
		
		// Establecemos el header con la configuracion establecida
		response.setHeader(headerKey, headerValue);
		
		// Obtenemos la lista de categorias del sistema 
		ResponseEntity<CategoryResponseRest> categoriesResponse = service.search();
		
		List<Category> listaCategorias = categoriesResponse.getBody().getCategoryResponse().getCategory();
		
		// Generamos fichero excel con la lista de categorias
		CategoryExcelExporter excelExporter = new CategoryExcelExporter(listaCategorias);
		
		excelExporter.export(response);
		
	}
	
}
