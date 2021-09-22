package int222.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
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
		if (photo != null) {
			file.deleteOne(oldProd.getImage());
			String photoname = file.save(photo);
			product.setImage(photoname);
		} else {
			product.setImage(oldProd.getImage());
		}

//		checkColors(product);  // Validate Step
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

//	private void checkColors(Product product) {
//	if (product.getProductcolor().isEmpty()) {
//		throw new DataRelatedException(ERROR_CODE.COLOR_DOESNT_FOUND, "Product does not contain any color!");
//	}
//}

}
