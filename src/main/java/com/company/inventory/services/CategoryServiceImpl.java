package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;

@Service
public class CategoryServiceImpl implements ICategoryService {

	// Inyectar el objeto al contenedor de Spring
	// El objeto queda instanciado de forma automatica
	@Autowired
	private ICategoryDao categoryDao;
	
	// Declaracion de metodo transaccional (acceso a bbdd)
	// Gestiona automaticamente los rollback en las excepciones
	//@Override
	@Transactional(readOnly=true)
	public ResponseEntity<CategoryResponseRest> search() {
		// TODO Auto-generated method stub

		CategoryResponseRest response = new CategoryResponseRest();
		
		try {
			
			List<Category> category = (List<Category>) categoryDao.findAll();
			
			response.getCategoryResponse().setCategory(category);
			
			// Rellenar el metadata de la respuesta
			response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
			
		} catch (Exception e) {
			
			response.setMetadata("Respuesta nok", "-1", "Error al consultar");
			e.getStackTrace();
			
			ResponseEntity<CategoryResponseRest> respError = new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			return respError;
			
		}
		
		ResponseEntity<CategoryResponseRest> respuesta = new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
		
		return respuesta;
		
	}

	/**
	 * Busqueda de una Categoria por Id
	 */
	@Transactional(readOnly=true)
	@Override
	public ResponseEntity<CategoryResponseRest> searchById(Long id) {
		// TODO Auto-generated method stub
		
		CategoryResponseRest response = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();
		
		try {
			
			Optional<Category> category = categoryDao.findById(id);
			
			if (category.isPresent()) {
				list.add(category.get());
				response.getCategoryResponse().setCategory(list);
				// Rellenar el metadata de la respuesta
				response.setMetadata("Respuesta ok", "00", "Categoria encontrada");
			} else {
				response.setMetadata("Respuesta nok", "-1", "Categoria no encontradda");
				ResponseEntity<CategoryResponseRest> respError = new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
				return respError;
			}
			
		} catch (Exception e) {
			
			response.setMetadata("Respuesta nok", "-1", "Error al consultar por Id");
			e.getStackTrace();
			
			ResponseEntity<CategoryResponseRest> respError = new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			return respError;
			
		}
		
		ResponseEntity<CategoryResponseRest> respuesta = new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
		
		return respuesta;
		
	}

	/**
	 * Guardar categoria
	 */
	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> save(Category category) {
		// TODO Auto-generated method stub
		
		CategoryResponseRest response = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();
		
		try {
			
			// Almacenamos registro en BBDD
			// Como respuesta, recibimos los datos del objeto almacenado
			Category categorySaved = categoryDao.save(category);
			
			// Si se ha almacenado correctamente, cargamos objeto en la lista de categorias que se va a devolver en la respuesta
			if (categorySaved != null) {
				list.add(categorySaved);
				response.getCategoryResponse().setCategory(list);
				
				response.setMetadata("Respuesta ok", "00", "Categoria almacenada");
			} else {
				response.setMetadata("Respuesta nok", "-1", "Categoria no almacenada");
				ResponseEntity<CategoryResponseRest> respError = 
											new ResponseEntity<CategoryResponseRest>(response, HttpStatus.BAD_REQUEST);
				return respError;
			}
				
		} catch (Exception e) {
			
			response.setMetadata("Respuesta nok", "-1", "Error al almacenar registro");
			e.getStackTrace();
			
			ResponseEntity<CategoryResponseRest> respError = new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			return respError;
			
		}
		
		ResponseEntity<CategoryResponseRest> respuesta = new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
		
		return respuesta;
		
	}

}
