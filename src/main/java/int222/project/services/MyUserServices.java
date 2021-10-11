package int222.project.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import int222.project.exceptions.DataRelatedException;
import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.models.Users;
import int222.project.repositories.UserJpaRepository;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor 
public class MyUserServices implements UserDetailsService {
	
	private final UserJpaRepository userRepo;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepo.findByUsername(username);
		if(user == null) { 
			throw new UsernameNotFoundException("User not found in the database.");
		} else {
			Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(user.getRole()));
			return new User(user.getUsername(), user.getPassword(), authorities);
		}
	}
	
	public Users addUser(Users user) {
		user.setPassword( passwordEncoder.encode(user.getPassword()) );
		user.setRole("ROLE_CUSTOMER");
		return userRepo.save(user);
	}
	
	public Users editUser(Users user) {
		return userRepo.save(user);
	}
	
	public Users getUser(String username) {
		return userRepo.findByUsername(username);
	}
	
	public List<Users> getAllUsers() {
		return userRepo.findAll();
	}
	
	public Users deleteUser(Integer uid) {
		Users user = userRepo.findById(uid).orElseThrow(() -> new DataRelatedException(ERROR_CODE.ITEM_DOES_NOT_EXIST, "User does not found."));
		user.setDeleted(1);
		return userRepo.save(user);
	}

}
