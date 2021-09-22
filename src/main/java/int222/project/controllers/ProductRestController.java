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

	// Get All Product with Paging
	@GetMapping("/products")
	public Page<Product> listAllWithPage(@RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "8") int size, @RequestParam(defaultValue = "pid") String sortBy) {
		return product.findAllProductWithPage(pageNo, size, sortBy);
	}

	//***********************//
	//*     Add Product     *//
	//***********************//
	@PostMapping(value = "/save", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public Product addProduct(@RequestParam(value = "photo", required = true)MultipartFile photo, 
									@RequestPart Product product) {
		return this.product.addProduct(photo, product);
	}

	//***********************//
	//*    Edit Product     *//
	//***********************//
	
	@PutMapping(value = "/edit", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public Product editProduct(@RequestParam(value = "photo", required = false)MultipartFile photo, 
			@RequestPart Product product) {
		return this.product.editProduct(photo, product);
	}

//
//	/*****************
//	 * Delete Method *
//	 *****************/
//
//	@DeleteMapping("/delete/{productcode}")
//	public Products removeProducts(@PathVariable Integer productcode) {
//		Products delProd = prodRepo.findById(productcode).orElse(null);
//		// Check if product is null then throw an exception.
//		if(delProd == null) {
//			throw new DataRelatedException(ERROR_CODE.PRODUCT_DOESNT_FOUND, "Cannot find product with productcode: "+productcode);
//		}
//		prodRepo.deleteById(productcode);
//		storeService.deleteOne(delProd.getImage());
//		return delProd;
//	}
}
