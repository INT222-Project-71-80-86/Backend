package int222.project.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
		validateAddUser(user);
		user.setPassword( passwordEncoder.encode(user.getPassword()) );
		user.setRole("ROLE_CUSTOMER");
		return userRepo.save(user);
	}
	
	public Users editUser(Users user) {
		validateEditUser(user);
		return userRepo.save(user);
	}
	
	public Users getUser(String username) {
		return userRepo.findByUsername(username);
	}
	
	public List<Users> getAllUsers() {
		return userRepo.findAll();
	}
	
	public Page<Users> gettAllUsersPaging(int pageNo, int size, String sortBy) {
		return userRepo.findAll(PageRequest.of(pageNo, size, Sort.by(sortBy)));
	}
	
	public Users deleteUser(Integer uid) {
		Users user = userRepo.findById(uid).orElseThrow(() -> new DataRelatedException(ERROR_CODE.USER_DOESNT_FOUND, "User does not found."));
		user.setDeleted(1);
		return userRepo.save(user);
	}
	
	private void validateAddUser(Users user) {
		Users tempUser = userRepo.findByUsername(user.getUsername());
		if(tempUser != null) {
			throw new DataRelatedException(ERROR_CODE.USER_ALREADY_EXIST, "This username has been used.");
		}
	}
	
	private void validateEditUser(Users user) {
		Users tempUser = userRepo.findById(user.getUid()).orElseThrow(() -> new DataRelatedException(ERROR_CODE.USER_DOESNT_FOUND, "User with user id "+user.getUid()+" does not found."));
	}

}
