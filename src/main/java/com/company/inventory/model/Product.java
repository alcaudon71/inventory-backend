package com.company.inventory.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="product")
@Data   // Lombok
public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7461389651533509262L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private int price;
	private int account;
	
	// Relacion productos-categorias N:1
	// JsonIgnoreProperties --> propiedades que no deben ser incluidas en el JSON del objeto producto
	@ManyToOne (fetch = FetchType.LAZY)
	@JsonIgnoreProperties ( {"hibernateLazyInitializater", "handler"} )
	private Category category;
	
	// Imagen del producto almacenada en formato base64 de 1000 bytes y capturada en atributo byte[]
	// En MySql se almacena como varbinary(1000) ---> limite maximo de un varbinary: 65000 bytes.
	// Hasta MySql 5 esto se almacenaba como tipo blob, desde MySql 6 se almacena como varbinary
	// ...
	// @Lob ---> BLOB - BINARY LONG OBJECT 
	// Este tag permite almacenar imagenes con un limite maximo de 4200 Mbytes
	// Esto crea en MySql un campo de tipo Longblob 
	// tinyblob		255 bytes
	// blob			65000 bytes
	// mediumblob				1600 Mbytes
	// longblob					4200 Mbytes
	// El tamaño maximo permitido en la app se define en application.properties
	@Lob
	@Basic (fetch = FetchType.LAZY)
	//@Column(name="picture", length=1000)
	@Column(name="picture", columnDefinition="longblob")
	private byte[] picture;
	
}
