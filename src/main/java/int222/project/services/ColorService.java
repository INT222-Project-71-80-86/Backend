package int222.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import int222.project.exceptions.DataRelatedException;
import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.models.Color;
import int222.project.repositories.ColorJpaRepository;

@Service
public class ColorService {

	// * Local Variable *//
	// Repositories //
	@Autowired
	private ColorJpaRepository colorRepo;

	// Methods //
	// Search One Product
	public Color findColorById(Integer bid) {
		return colorRepo.findById(bid).orElseThrow(() -> 
		new DataRelatedException(ERROR_CODE.BRAND_DOESNT_FOUND, "Color Id: " + bid + " does not exist."));
	}

	// Get all colors 
	public List<Color> findAllColors() {
		return colorRepo.findAll();
	}

//	// Search All Color With Paging
//	public Page<Color> findAllProductWithPage(int pageNo, int size, String sortBy) {
//		return colorRepo.findAll(PageRequest.of(pageNo, size, Sort.by(sortBy)));
//	}

	// Add Color
	public Color addColor(Color color) {
		checkColorDuplicate(color);
		return colorRepo.saveAndFlush(color);
	}
	
	// Edit Product
	public Color editColor(Color color) {
		checkColorDuplicate(color);
		return colorRepo.saveAndFlush(color);
	}
	
	private void checkColorDuplicate(Color color) {
		if(!colorRepo.findByNameOrCode(color.getName(),color.getCode()).isEmpty()) {
			throw new DataRelatedException(ERROR_CODE.COLOR_ALREADY_EXIST, "Color with this name: "
		+ color.getName()+" or this code:" + color.getCode() + " is already exist.");
		}
	}

}
