package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.util.Util;

@Service
public class ProductServiceImpl implements IProductService {

	private ICategoryDao categoryDao;
	private IProductDao productDao;
	
	// Inyeccion de categoryDao por constructor, equivalente a usar el @Autowired
	public ProductServiceImpl(ICategoryDao categoryDao, IProductDao productDao) {
		super();
		this.categoryDao = categoryDao;
		this.productDao = productDao;
	}

	/**
	 * Almacenar Producto
	 * @param 	Producto		Producto a almacenar
	 * @param 	Long			Id de la categoria del producto
	 * @return	ResponseEntity	Respuesta con el producto
	 */
	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {
		// TODO Auto-generated method stub
		
		ProductResponseRest response = new ProductResponseRest();
		
		List<Product> list = new ArrayList<>();
		
		try {
			
			// Buscar categoria que debe establecerse en el objeto Producto
			Optional<Category> category = categoryDao.findById(categoryId);
			
			if (category.isPresent()) {
				product.setCategory(category.get());
			} else {
				response.setMetadata("respuesta nok", "-1", "Categoria no encontrada asociada al producto");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
			// Almacenar el producto en BBDD
			Product productSaved = productDao.save(product);
			
			if (productSaved != null) {
				list.add(productSaved);
				// Almacenamos la lista de productos en el objeto ProductResponse para la aplicacion cliente
				response.getProductResponse().setProducts(list); 
				response.setMetadata("respuesta ok", "00", "Producto almacenado");
			} else {
				response.setMetadata("respuesta nok", "-1", "Producto no almacenado");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST); // error 404
			}
			
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok", "-1", "Error al guardar producto");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR); // error 500
		}
		
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK); 
		
	}

	/**
	 * Buscar por Id de Producto
	 * @param 	Long			Id del producto
	 * @return	ResponseEntity	Respuesta con el producto
	 */
	@Override
	@Transactional (readOnly = true)
	public ResponseEntity<ProductResponseRest> searchById(Long productId) {
		// TODO Auto-generated method stub
		
		ProductResponseRest response = new ProductResponseRest();
		
		List<Product> list = new ArrayList<>();
		
		try {
			
			// Buscar Producto por id
			Optional<Product> product = productDao.findById(productId);
			
		    System.out.println("product: " + product.toString());
			
			if (product.isPresent()) {
				
				// Cambiamos la imagen del producto del formato BBDD al formato legible por la web
				byte[] imageBbdd = product.get().getPicture();
				byte[] imageDescompressed = Util.decompressZLib(imageBbdd);
				product.get().setPicture(imageDescompressed);
				
				System.out.println("product & image: " + product.toString());
				
				list.add(product.get());
				
				// Cargamos la info de la respuesta
				response.setMetadata("respuesta ok", "00", "Producto encontrado");
				response.getProductResponse().setProducts(list);
				
				
			} else {
				response.setMetadata("respuesta nok", "-1", "Producto no encontrado");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok", "-1", "Error al recuperar producto");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR); // error 500
		}
		
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK); 
		
	}

}
