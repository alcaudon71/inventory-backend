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

	
	/**
	 * Busqueda de productos cuyo nombre contega un literal especificado
	 * @param 	cadena			Literal por el que se busca en el nombre de los productos
	 * @return  ResponseEntity 	Respuesta con la lista de productos encontrada
	 */
	@Override
	@Transactional (readOnly = true)
	public ResponseEntity<ProductResponseRest> searchByName(String cadena) {
		// TODO Auto-generated method stub
		
		ProductResponseRest response = new ProductResponseRest();
		
		List<Product> list = new ArrayList<>();
		List<Product> listAux = new ArrayList<>();
		
		try {
			
			// Buscar Producto por cadena de Nombre
			listAux = productDao.findByNameContainingIgnoreCase(cadena);
			
			if (listAux.size() > 0) {
			
				// Recorremos la lista recuperando la info de cada producto con una funcion lambda
				listAux.stream().forEach( (product) -> {
					// Cambiamos la imagen del producto del formato BBDD al formato legible por la web
					byte[] imageBbdd = product.getPicture();
					byte[] imageDescompressed = Util.decompressZLib(imageBbdd);
					product.setPicture(imageDescompressed);
					list.add(product);
				} );

				// Cargamos la info de la respuesta
				response.setMetadata("respuesta ok", "00", "Productos encontrados");
				response.getProductResponse().setProducts(list);
				
				
			} else {
				response.setMetadata("respuesta nok", "-1", "Productos no encontrados");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok", "-1", "Error al recuperar productos por literal");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR); // error 500
		}
		
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK); 
		
	}

	/**
	 * Eliminar producto por id
	 * @param	id				Id del producto
	 * @return	ResponseEntity	Respuesta con los metadatos de la ejecucion
	 */
	@Override
	@Transactional 
	public ResponseEntity<ProductResponseRest> deleteById(Long id) {
		// TODO Auto-generated method stub
		
		ProductResponseRest response = new ProductResponseRest();
		
		try {
			
			// Eliminar Producto por id
			productDao.deleteById(id);
			
			response.setMetadata("respuesta ok", "00", "Producto eliminado");
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok", "-1", "Error al eliminar producto");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR); // error 500
		}
		
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK); 
		
	}

	/**
	 * Obtencion de todos los productos contenidos en la BBDD
	 * @return	ResponseEntity	Respuesta con todos los productos de la BBDD
	 */
	@Override
	@Transactional (readOnly = true)
	public ResponseEntity<ProductResponseRest> searchAll() {
		// TODO Auto-generated method stub
		
		ProductResponseRest response = new ProductResponseRest();
		
		List<Product> list = new ArrayList<>();
		List<Product> listAux = new ArrayList<>();
		
		try {
			
			// Obtenemos todos los productos de la BBDD
			listAux = (List<Product>) productDao.findAll();
			
			if (listAux.size() > 0) {
			
				// Recorremos la lista recuperando la info de cada producto con una funcion lambda
				listAux.stream().forEach( (product) -> {
					// Cambiamos la imagen del producto del formato BBDD al formato legible por la web
					byte[] imageBbdd = product.getPicture();
					byte[] imageDescompressed = Util.decompressZLib(imageBbdd);
					product.setPicture(imageDescompressed);
					list.add(product);
				} );

				// Cargamos la info de la respuesta
				response.setMetadata("respuesta ok", "00", "Productos encontrados");
				response.getProductResponse().setProducts(list);
				
				
			} else {
				response.setMetadata("respuesta nok", "-1", "Productos no encontrados");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);		// error 404
			}
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok", "-1", "Error al recuperar todos los productos");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR); // error 500
		}
		
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK); 
		
	}

	/**
	 * Actualizar producto con los datos enviados
	 * @param	product			Datos con los que debe ser actualizado el producto
	 * @param	categoryId		Id de la nueva categoria del producto
	 * @param	productId		Id del producto que debe ser actualizado
	 * @param	ResponseEntity	Respuesta con la lista de productos actualizados
	 */
	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> update(Product product, Long categoryId, Long productId) {
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
			
			// Buscar producto a actualizar
			Optional<Product> productSearch = productDao.findById(productId);
			
			// Actualizamos los datos del producto recuperado con los datos recibidos en el metodo
			if (productSearch.isPresent()) {
				
				productSearch.get().setName(product.getName());
				productSearch.get().setAccount(product.getAccount());
				productSearch.get().setPrice(product.getPrice());
				productSearch.get().setCategory(category.get());
				productSearch.get().setPicture(product.getPicture());
				
				// Almacenamos producto en base de datos
				Product productToUpdate = productDao.save(productSearch.get());
				
				// Si el producto se ha actualizado  correctamente
				if (productToUpdate != null){
					list.add(productToUpdate);

					// Almacenamos la lista de productos en el objeto ProductResponse para la aplicacion cliente
					response.getProductResponse().setProducts(list); 
					response.setMetadata("respuesta ok", "00", "Producto actualizado");
				} else {
					response.setMetadata("respuesta nok", "-1", "Producto no actualizado");
					return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST); // error 404
				}
				
			} else {
				response.setMetadata("respuesta nok", "-1", "Producto no actualizado");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST); // error 404
			}
			
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok", "-1", "Error al actualizar producto");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR); // error 500
		}
		
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK); 
		
	}

	
}
