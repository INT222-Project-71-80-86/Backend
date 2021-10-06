package int222.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	// Add Color
	public Color addColor(Color color) {
		validateColor(color);
		return colorRepo.saveAndFlush(color);
	}
	
	// Edit Product
	public Color editColor(Color color) {
		validateColor(color);
		return colorRepo.saveAndFlush(color);
	}
	
	private void validateColor(Color color) {
		if(!colorRepo.findByNameOrCode(color.getName(),color.getCode()).isEmpty()) {
			throw new DataRelatedException(ERROR_CODE.COLOR_ALREADY_EXIST, "Color with this name: "
		+ color.getName()+" or this code:" + color.getCode() + " is already exist.");
		}
		String message = "";
		if(color.getCode().length() != 7 || color.getName().length() > 200) {
			throw new DataRelatedException(ERROR_CODE.INVALID_ATTRIBUTE, 
					"Invalid attribute. Color code should be in form of '#XXXXXX' and it name length must not has more than 200 characters");
		}
	}
	
	// Delete Color
 	public Color deleteColor(Integer cid) {
 		Color c = colorRepo.findById(cid).orElseThrow(() -> new DataRelatedException(ERROR_CODE.COLOR_DOESNT_FOUND, 
 				"Color with id: "+cid+" doesn't found"));
 		colorRepo.delete(c);
 		return c;
 	}
 
}
