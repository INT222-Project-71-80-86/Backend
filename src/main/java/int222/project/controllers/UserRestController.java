package int222.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import int222.project.models.Users;
import int222.project.services.MyUserServices;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
	
	@Autowired
	private MyUserServices userService;
	
	@PostMapping("/save")
	public Users saveUser(@RequestBody Users user) {
		return userService.addUser(user);
	}

}
