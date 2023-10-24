package com.company.inventory.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.services.IProductService;
import com.company.inventory.util.Util;

//CrossOrigin ---> Permite el acceso desde una aplicacion FrontEnd externa a nuestro controlador BackEnd
//Habilitamos el acceso desde localhost:4200, que es el puerto por defecto utilizado por los FrontEnd de Angulars
@CrossOrigin(origins = {"http://localhost:4200"} )
@RestController
@RequestMapping("/api/v1")
public class ProductRestController {

	private IProductService productService;
	
	// Inyeccion por constructor
	public ProductRestController(IProductService productService) {
		super();
		this.productService = productService;
	}

	/**
	 * Controlador para invocar al servicio de Almacenamiento de Producto
	 * 		Se reciben los datos asociados a un producto
	 * @param  	picture			Recibimos foto en formato manipulable por Spring Boot
	 * @param	name 			Nombre del Producto
	 * @param	price			Precio del Producto
	 * @param	account			Cantidad del Producto
	 * @param	categoryId		Identificador de Categoria del Producto
	 * @return	ResponseEntity 	Datos de los productos buscados
	 * @throws 	IOException
	 */
	// endpoint: /api/v1/products/
	// @PathVariable ---> recoge el endpoint de entrada en formato "Decoded"
	//                    http://localhost:8080/api/v1/products
	// @RequestParam ---> recoge el endpoint de entrada en formato "Encoded"
	//  				  http://localhost:8080/api/v1/products
	@PostMapping("/products")
	public ResponseEntity<ProductResponseRest> save(
			@RequestParam("picture") MultipartFile picture,
			@RequestParam("name") String name,
			@RequestParam("price") int price,
			@RequestParam("account") int account,
			@RequestParam("categoryId") Long categoryId ) throws IOException {
		
		Product product = new Product();
		
		// Comprimir foto a formato base64 para almacenar en BBDD
		byte[] pictureBbdd = Util.compressZLib(picture.getBytes());
		
		product.setName(name);
		product.setAccount(account);
		product.setPrice(price);
		product.setPicture(pictureBbdd);
		
		// Invocamos al servicio de Almacenar Producto
		ResponseEntity<ProductResponseRest> response = productService.save(product, categoryId);
		
		return response;
		
	}
	
	/**
	 * Obtener Producto por Id
	 * @param 	id				Identificador del Producto
	 * @return 	ResponseEntity	Respuesta con el Producto recuperado
	 */
	// endpoint: /api/v1/products/{id}
	// @PathVariable ---> recoge el endpoint de entrada en formato "Decoded"
	//                    http://localhost:8080/api/v1/products/abc
	// @RequestParam ---> recoge el endpoint de entrada en formato "Encoded"
	//  				  http://localhost:8080/api/v1/products?id=abc
	@GetMapping("products/{id}")
	public ResponseEntity<ProductResponseRest> searchById(@PathVariable Long id) {
		
		// Invocamos al servicio para recuperar el Producto correspondiente al Id
		ResponseEntity<ProductResponseRest> response = productService.searchById(id);
		
		System.out.println("ProductResponse: " + response.toString() );
		
		return response;
		
	}
	
}
