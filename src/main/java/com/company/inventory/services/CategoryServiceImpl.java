package com.company.inventory.services;

import java.util.List;

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

}
