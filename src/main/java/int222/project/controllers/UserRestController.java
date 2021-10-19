package int222.project.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
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
		return userService.addUser(user);
	}
	
	@PutMapping("/edit")
	public Users editUser(@RequestBody Users user, @RequestAttribute String username) {
		return userService.editUser(user, username);
	}
	
	@GetMapping("/allusers")
	public Page<Users> getAllUsers(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int size, 
			@RequestParam(defaultValue = "uid") String sortBy) {
		return userService.getAllUsersPaging(pageNo, size, sortBy);
	}
	
	@GetMapping("/get")
	public Users getUserByUsername(@RequestAttribute String username) {
		return userService.getUser(username);
	}
	
	@PutMapping("/roleedit")
	public Users editUserRole(@RequestBody Users user) {
		return userService.changeRole(user);
	}
	
	 @GetMapping("/token/refresh")
	    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		 userService.refreshTokenV2(request, response);
	 }

}
