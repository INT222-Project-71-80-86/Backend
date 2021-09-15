package int222.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import int222.project.exceptions.DataRelatedException;
import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.models.Product;
import int222.project.models.Productcolor;
import int222.project.models.ProductcolorPK;
import int222.project.repositories.ProductJpaRepository;

@Service
public class ProductService {

	// * Local Variable *//
	// Repositories //
	@Autowired
	private ProductJpaRepository prodRepo;
	// Services //
	@Autowired
	private FileStoreServiceImp file;

	// Methods //
	// Search One Product
	public Product findProductById(int pid) {
		Product p = prodRepo.findById(pid).orElseThrow(() -> new DataRelatedException(ERROR_CODE.PRODUCT_DOESNT_FOUND,
				"Product number " + pid + " does not exist."));
		return p;
	}

	// Search All Product
	public List<Product> findAllProduct() {
		return prodRepo.findAll();
	}

	// Search All Product With Paging
	public Page<Product> findAllProductWithPage(int pageNo, int size, String sortBy) {
		return prodRepo.findAll(PageRequest.of(pageNo, size, Sort.by(sortBy)));
	}

	// Add Product
	public Product addProduct(MultipartFile photo, Product product) {
		String filename = file.save(photo); // Store photo and get randomize filename.
		product.setImage(filename); // set randomized filename to product.
//		checkColors(product); // Validate Step
		product.setPid(0);
		addPrimaryKey(product);
		return prodRepo.saveAndFlush(product);
	}

	// Edit Product
	public Product editProduct(MultipartFile photo, Product product) {
		Product oldProd = prodRepo.findById(product.getPid()).orElse(null);
		if (oldProd == null) {
			throw new DataRelatedException(ERROR_CODE.PRODUCT_DOESNT_FOUND,
					"Product with code: " + product.getPid() + " does not exists.");
		}
		// If send photo. delete old and add new. If not just use old.
		if(photo != null) {
			file.deleteOne(oldProd.getImage());
			String photoname = file.save(photo);
			product.setImage(photoname);
		} else {
			product.setImage(oldProd.getImage());
		}
		
//		checkColors(product);  // Validate Step
		addPrimaryKey(product);
//		resetProductcode(product.getPid()); 
		return prodRepo.saveAndFlush(product);
	}
	
	// Add ProductColor's Color Primary Key
	private void addPrimaryKey(Product product) {
		for (Productcolor p : product.getProductcolor()) {
			System.out.println(p.getColor().getName());
			p.setId(new ProductcolorPK(product.getPid(), p.getColor().getCid()));
			p.setProduct(product);
		}
	}
}
