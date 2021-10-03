package int222.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import int222.project.models.Product;
import int222.project.services.ProductService;

@RequestMapping(path = "/api/product")
@RestController
public class ProductRestController {
	
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
	@GetMapping("/{pid}")
	public Product searchOne(@PathVariable int pid) {
		return product.findProductById(pid);
	}

	// Get All Product with Paging
	@GetMapping("")
	public Page<Product> listAllWithPage(@RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "pid") String sortBy) {
		return product.findAllProductWithPage(pageNo, size, sortBy);
	}
	
	// Search Product By Query values
	@GetMapping("/query")
	public Page<Product> findAllWithSearchValue(@RequestParam String searchValue, @RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "pid") String sortBy){
		return product.findAllProductContainsParams(searchValue,pageNo, size, sortBy);
	}
	
	// Filter Product by brand
	@GetMapping("/brand")
	public Page<Product> filterByBrand(@RequestParam(name = "id") Integer bid, @RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "pid") String sortBy){
		return product.filterProductByBrand(bid, pageNo, size, sortBy);
	}
	
	// Filter Product by brand and categories
	@GetMapping("/brandcat")
	public Page<Product> filterProductByBrandAndCategory(@RequestParam(name = "bid") Integer bid, 
			@RequestParam(name = "catid") Integer catid, @RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "pid") String sortBy){
		return product.filterProductByBrandAndCategory(bid,catid,pageNo,size,sortBy);
	}

	@GetMapping("/cat")
	public Page<Product> filterByCategory(@RequestParam(name = "id") Integer catid, @RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "pid") String sortBy){
		return product.filterProductByCategory(catid, pageNo, size, sortBy);
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

	//**************************//
	//*     Remove Product     *//
	//**************************//
	@DeleteMapping("/delete/{pid}")
	public Product removeProduct(@PathVariable Integer pid) {
		return this.product.removeProducts(pid);
	}
	
	//*****************************//
	//*     Get Product Image     *//
	//*****************************//
	@GetMapping("/image/{pid}")
	public ResponseEntity<Resource> getFileFromPid(@PathVariable Integer pid){
		return this.product.getFileFromPid(pid);
	}
	
}
