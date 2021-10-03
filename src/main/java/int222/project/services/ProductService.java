package int222.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import int222.project.exceptions.DataRelatedException;
import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.models.Product;
import int222.project.models.Productcolor;
import int222.project.models.ProductcolorPK;
import int222.project.repositories.ProdColorJpaRepository;
import int222.project.repositories.ProductJpaRepository;

@Service
public class ProductService {

	// * Local Variable *//
	// Repositories //
	@Autowired
	private ProductJpaRepository prodRepo;
	@Autowired
	private ProdColorJpaRepository pcRepo;
	// Services //
	@Autowired
	private FileStoreServices file;

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
	
	//Search Product with search filter (from text box)
	public Page<Product> findAllProductContainsParams(String searchValue, int pageNo, int size, String sortBy){
		//Search Product within field description, name, brand.name, cat.name
		Page<Product> p = prodRepo.findProductByString(searchValue,PageRequest.of(pageNo, size, Sort.by(sortBy)));
		return p;
	}
	
	//Filter Product By Brand Only
	public Page<Product> filterProductByBrand(Integer bid, int pageNo, int size, String sortBy){
		Page<Product> p = prodRepo.findProductByBrandBid(bid, PageRequest.of(pageNo, size, Sort.by(sortBy)));
		return p;
	}
	
	public Page<Product> filterProductByCategory(Integer catid, int pageNo, int size, String sortBy){
		Page<Product> p = prodRepo.findProductByCategoryCatid(catid, PageRequest.of(pageNo, size, Sort.by(sortBy)));
		return p;
	}
	
	public Page<Product> filterProductByBrandAndCategory(Integer bid, Integer catid, int pageNo, int size, String sortBy) {
		Page<Product> p = prodRepo.findProductByBrandBidAndCategoryCatid(bid, catid, PageRequest.of(pageNo, size, Sort.by(sortBy)));
		return p;
	}

	// Add Product
	public Product addProduct(MultipartFile photo, Product product) {
		String filename = file.save(photo); // Store photo and get randomize filename.
		product.setImage(filename); // set randomized filename to product.
		validateAttribute(product);
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
		// If send photo add new. If not just use old.
		if (photo != null) {
			String photoname = file.save(photo);
			product.setImage(photoname);
		} else {
			product.setImage(oldProd.getImage());
		}
		validateAttribute(product);
		file.deleteOne(oldProd.getImage()); // If Validate Succesfully Remove old image
		addPrimaryKey(product);
		pcRepo.removeByIdPid(product.getPid());
		return prodRepo.saveAndFlush(product);
	}

	// Remove product
	public Product removeProducts(Integer pid) {
		Product delProd = prodRepo.findById(pid).orElseThrow(() -> new DataRelatedException(ERROR_CODE.PRODUCT_DOESNT_FOUND,
				"Cannot find product with productcode: " + pid));
		prodRepo.deleteById(pid);
		file.deleteOne(delProd.getImage());
		return delProd;
	}

	// Add ProductColor's Color Primary Key
	private void addPrimaryKey(Product product) {
		for (Productcolor p : product.getProductcolor()) {
			p.setId(new ProductcolorPK(product.getPid(), p.getColor().getCid()));
			p.setProduct(product);
		}
	}

	// For validating product [ validating productcolor because it can save when no productcolor is present 
	// and image because adding new random string to make it distinct 
	private void validateAttribute(Product product) {
		if (product.getProductcolor().isEmpty()) {
			file.deleteOne(product.getImage());
			throw new DataRelatedException(ERROR_CODE.COLOR_DOESNT_FOUND, "Product does not contain any color!");
		} else if (product.getImage().length() > 200) {
			file.deleteOne(product.getImage());
			throw new DataRelatedException(ERROR_CODE.INVALID_PRODUCT_ATTRIBUTE, "File name is too long!");
		}
	}
	
	// Get image from pid
	public ResponseEntity<Resource> getFileFromPid(Integer pid){
		Product p = prodRepo.findById(pid).orElseThrow(() -> new DataRelatedException(ERROR_CODE.PRODUCT_DOESNT_FOUND,
				"Product number " + pid + " does not exist."));
		Resource file = this.file.load(p.getImage()); // Get Resource File
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
	}



}
