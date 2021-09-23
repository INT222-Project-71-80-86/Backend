package int222.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import int222.project.exceptions.DataRelatedException;
import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.models.Color;
import int222.project.repositories.ColorJpaRepository;
import int222.project.services.ColorService;

@RequestMapping(path = "/api/color")
@RestController
public class ColorRestController {
	
	@Autowired ColorService colorservice;
	
	// Get all colors 
	@GetMapping("")
	public List<Color> getColors() {
		return colorservice.findAllColors();
	}
	
	// Search Color By Id
	@GetMapping("/{bid}")
	public Color getColor(@PathVariable Integer bid) {
		return colorservice.findColorById(bid);
	}
	
	// Add Color
	@PostMapping("/save")
	public Color addColor(@RequestBody Color color) {
		return colorservice.addColor(color);
	}
	
	// Edit Color
	@PutMapping("/edit")
	public Color editColor(@RequestBody Color color) {
		return colorservice.editColor(color);
	}
	

}
