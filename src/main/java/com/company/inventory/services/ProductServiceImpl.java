package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;

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
	 */
	@Override
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

}
