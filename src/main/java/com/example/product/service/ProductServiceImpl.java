package com.example.product.service;

import com.example.product.dto.ProductDto;
import com.example.product.entity.Category;
import com.example.product.entity.Product;
import com.example.product.entity.Product.Status;
import com.example.product.exception.ResouceNotFoundException;
import com.example.product.repo.CategoryRepo;
import com.example.product.repo.ProductRepo;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl {

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	public Product createProduct(ProductDto dto) throws ResouceNotFoundException {
		Category category = categoryRepo.findById(dto.getCategoryId())
				.orElseThrow(() -> new ResouceNotFoundException("Category not found"));

		Product product = new Product();
		product.setName(dto.getName());
		product.setSku(dto.getSkuCode());
		product.setDescription(dto.getDescription());
		product.setCategory(category);
		product.setPrice(BigDecimal.valueOf(dto.getPrice()));
		product.setStock(dto.getStockQuantity());
		product.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));

		return productRepo.save(product);
	}

	public Product getProductById(Long id) throws ResouceNotFoundException {
		return productRepo.findById(id)
				.orElseThrow(() -> new ResouceNotFoundException("Product not found with id " + id));
	}

	public Product updateProduct(Long id, ProductDto dto) throws ResouceNotFoundException {
		Product product = getProductById(id);

		Category category = categoryRepo.findById(dto.getCategoryId())
				.orElseThrow(() -> new ResouceNotFoundException("Category not found"));

		product.setName(dto.getName());
		product.setSku(dto.getSkuCode());
		product.setDescription(dto.getDescription());
		product.setCategory(category);
		product.setPrice(BigDecimal.valueOf(dto.getPrice()));
		product.setStock(dto.getStockQuantity());
		product.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));

		return productRepo.save(product);
	}

	public void deleteProduct(Long id) throws ResouceNotFoundException {
		Product product = getProductById(id);
		productRepo.delete(product);
	}

	public Page<Product> getAllProducts(int offset, int pageSize) {
		return productRepo.findAll(PageRequest.of(offset, pageSize));
	}

	public List<Product> searchProduct(String name) {
		return productRepo.findByNameContainingIgnoreCase(name);
	}

	public ResponseEntity<?> importProductsFromExcel(MultipartFile file) {
		List<String> errors = new ArrayList<>();
		List<Product> validProducts = new ArrayList<>();

		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				try {
					String sku = getCellValue(row.getCell(0));
					String name = getCellValue(row.getCell(1));
					String categoryName = getCellValue(row.getCell(2));
					String priceStr = getCellValue(row.getCell(3));
					String stockStr = getCellValue(row.getCell(4));
					String desc = getCellValue(row.getCell(5));

					if (sku.isEmpty() || name.isEmpty() || categoryName.isEmpty() || priceStr.isEmpty()
							|| stockStr.isEmpty()) {
						errors.add("Row " + (i + 1) + ": Required field is missing.");
						continue;
					}

					Category category = categoryRepo.findByName(categoryName);
					if (category == null) {
						errors.add("Row " + (i + 1) + ": Category '" + categoryName + "' not found.");
						continue;
					}

					Product p = new Product();
					p.setSku(sku);
					p.setName(name);
					p.setCategory(category);
					p.setPrice(new BigDecimal(priceStr));
					p.setStock(Integer.parseInt(stockStr));
					p.setDescription(desc);

					validProducts.add(p);

				} catch (Exception e) {
					errors.add("Row " + (i + 1) + ": " + e.getMessage());
				}
			}

			if (!validProducts.isEmpty())
				productRepo.saveAll(validProducts);

			Map<String, Object> result = new HashMap<>();
			result.put("imported", validProducts.size());
			result.put("errors", errors);

			return ResponseEntity.ok(result);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Import failed: " + e.getMessage());
		}
	}

	private String getCellValue(Cell cell) {
		if (cell == null)
			return "";
		return switch (cell.getCellType()) {
		case STRING -> cell.getStringCellValue().trim();
		case NUMERIC -> String.valueOf(cell.getNumericCellValue()).replace(".0", "").trim();
		case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
		default -> "";
		};
	}

	public void downloadExcelTemplate(HttpServletResponse response) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Products");

		Row header = sheet.createRow(0);
		String[] columns = { "SKU", "Name", "Category Name", "Price", "Stock", "Description" };

		for (int i = 0; i < columns.length; i++) {
			Cell cell = header.createCell(i);
			cell.setCellValue(columns[i]);
		}

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=product_import_template.xlsx");

		workbook.write(response.getOutputStream());
		workbook.close();
	}

}
