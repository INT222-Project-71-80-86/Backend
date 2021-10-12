package int222.project.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
		System.out.println(user);
		return userService.addUser(user);
	}
	
	@GetMapping("/allusers")
	public Page<Users> getAllUsers(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int size, 
			@RequestParam(defaultValue = "uid") String sortBy) {
		return userService.getAllUsersPaging(pageNo, size, sortBy);
	}
	
	@GetMapping("/get/{username}")
	public Users getUserByUsername(@PathVariable String username) {
		return userService.getUser(username);
	}
	
	 @GetMapping("/token/refresh")
	    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		 userService.refreshToken(request, response);
	 }

}
