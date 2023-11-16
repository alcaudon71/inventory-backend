package com.company.inventory.util;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.company.inventory.model.Category;
import com.company.inventory.model.Product;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class ProductExcelExporter {

	// Objetos para libros y hojas excel
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<Product> product;
	
	public ProductExcelExporter(List<Product> products) {
		this.product = products;
		
		workbook = new XSSFWorkbook();
		
	}
	
	/**
	 * Creacion de la fila cabecera de la hoja Excel
	 */
	private void writeHeaderLine() {
		// Creamos la hoja excel
		sheet = workbook.createSheet("Resultado");
		
		// Crear primera fila de la hoja excel
		Row row = sheet.createRow(0);
		
		// Estilo de las celdas
		CellStyle style = workbook.createCellStyle();
		
		// Creamos y configuramos fuente 
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		
		// Establecemos fuente del estilo
		style.setFont(font);
		
		// Creamos celdas de la primera fila
		createCell(row, 0, "ID", style);
		createCell(row, 1, "Nombre", style);
		createCell(row, 2, "Precio", style);
		createCell(row, 3, "Cantidad", style);
		createCell(row, 4, "Categoria", style);
		
	}
	
	/**
	 * Crear Celda de hoja excel
	 * @param row			Fila afectada
	 * @param columnCount	Columna afectada
	 * @param value			Valor a insertar en la celda
	 * @param style			Estilo a configurar en la celda
	 */
	private void createCell (Row row, int columnCount, Object value, CellStyle style) {
		
		sheet.autoSizeColumn(columnCount);
		
		/// Crear celda dentro de la columna
		Cell cell = row.createCell(columnCount);
		
		// Asignar valor
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		
		// Asignar estilo
		cell.setCellStyle(style);
				
	}
	
	/**
	 * Creacion de filas excel necesarias para almacenar la lista de Categorias
	 */
	private void writeDataLines() {
		
		int rowCount = 1;
		
		CellStyle style = workbook.createCellStyle();
		
		// Creamos fuente
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		
		// Asignamos fuente al estilo
		style.setFont(font);
		
		// Recorremos la lista de productos que hay que almacenar en el excel
		for (Product result: product) {
			// Crear fila siguiente
			Row row = sheet.createRow(rowCount);
			rowCount++;
			
			int columnCount = 0;
			
			// Creamos las celdas de la fila
			createCell(row, columnCount++, String.valueOf(result.getId()), style);
			createCell(row, columnCount++, String.valueOf(result.getName()), style);
			createCell(row, columnCount++, String.valueOf(result.getPrice()), style);
			createCell(row, columnCount++, String.valueOf(result.getAccount()), style);
			createCell(row, columnCount++, String.valueOf(result.getCategory().getName()), style);
			
		}
		
	}
	
	/**
	 * Exportar a fichero Excel
	 * @param response		Respuesta Http Servlet
	 * @throws IOException
	 */
	public void export(HttpServletResponse response) throws IOException {
	
		// Escribir Cabecera excel y filas de categorias
		writeHeaderLine();
		writeDataLines();
		
		ServletOutputStream servletOutputStream = response.getOutputStream();
		
		workbook.write(servletOutputStream);
		workbook.close();
		
		servletOutputStream.close();
		
	}
	
}
