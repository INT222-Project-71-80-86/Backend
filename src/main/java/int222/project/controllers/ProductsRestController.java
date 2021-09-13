package int222.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import int222.project.exceptions.DataRelatedException;
import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.models.Product;
import int222.project.repositories.ProductJpaRepository;
import int222.project.services.ProductService;

@RestController
public class ProductsRestController {
	
	//**************************//
	//*     Local Variable     *//
	//**************************//
	//         Services         //
	@Autowired
	private ProductService product;
	
	//**************************//
	//*     Search Product     *//
	//**************************//
	
	// Search One Product 
	@GetMapping("/product/{pid}")
	public Product searchOne(@PathVariable int pid) {
		return product.findProductById(pid);
	}
	
	// List All Product
	@GetMapping("/products")
	public List<Product> listAll() {
		return product.findAllProduct();
	}

}
