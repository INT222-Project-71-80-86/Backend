package int222.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import int222.project.exceptions.DataRelatedException;
import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.models.Product;
import int222.project.repositories.ProductJpaRepository;

@Service
public class ProductService {

	//**************************//
	//*     Local Variable     *//
	//**************************//
	
	//       Repositories       //
	@Autowired
	private ProductJpaRepository prodRepo;
	
	//         Methods          //
	// Search One Product 
	public Product findProductById(int pid) {
		Product p = prodRepo.findById(pid).orElseThrow(() -> new DataRelatedException(ERROR_CODE.PRODUCT_DOESNT_FOUND, 
				"Product number " + pid + " does not exist."));
		return p;
	}
	
	// Search All Product 
	public List<Product> findAllProduct(){
		return prodRepo.findAll();
	}
	
}
