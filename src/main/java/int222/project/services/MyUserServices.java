package int222.project.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;

import int222.project.exceptions.DataRelatedException;
import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.models.Users;
import int222.project.repositories.UserJpaRepository;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor 
public class MyUserServices implements UserDetailsService {
	
	private final UserJpaRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	@Value("${jwt.token.secret}") private String secret;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepo.findByUsername(username);
		if(user == null || user.getDeleted() != 0) { 
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
	
	public Users editUser(Users user, String username) {
		if(!user.getUsername().equals(username)) {
			throw new DataRelatedException(ERROR_CODE.USER_NOT_THE_SAME, "Cannot edit other user except yourself");
		}
		validateEditUser(user, false);
		return userRepo.save(user);
	}
	
	public Users changeRole(Users user) {
		validateEditUser(user, true);
		return userRepo.save(user);
	}

	public Users getUser(String username) {
		return userRepo.findByUsername(username);
	}
	
	public List<Users> getAllUsers() {
		return userRepo.findAll();
	}
	
	public Page<Users> getAllUsersPaging(int pageNo, int size, String sortBy) {
		return userRepo.findAll(PageRequest.of(pageNo, size, Sort.by(sortBy)));
	}
	
	public Users deleteUser(Integer uid) {
		Users user = userRepo.findById(uid).orElseThrow(() -> new DataRelatedException(ERROR_CODE.USER_DOESNT_FOUND, "User does not found."));
		user.setDeleted(1);
		return userRepo.save(user);
	}
	
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
            	System.out.println("Refresh Token Activated");
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                Users user = getUser(username);
                List<String> strings = new ArrayList<>();
                strings.add(user.getRole());
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000 ))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", strings)
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
    
    public void refreshTokenV2(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	Cookie[] cookies = request.getCookies();
    	for (Cookie cookie : cookies) {
			if(cookie.getName().equals("refresh_token")) {
				try {
//					System.out.println("REFRESH TOKEN ACTIVATED");
	                String refresh_token = cookie.getValue();
	                Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
	                JWTVerifier verifier = JWT.require(algorithm).build();
	                DecodedJWT decodedJWT = verifier.verify(refresh_token);
	                String username = decodedJWT.getSubject();
	                Users user = getUser(username);
	                List<String> strings = new ArrayList<>();
	                strings.add(user.getRole());
	                String access_token = JWT.create()
	                        .withSubject(user.getUsername())
	                        .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000 ))
	                        .withIssuer(request.getRequestURL().toString())
	                        .withClaim("roles", strings)
	                        .sign(algorithm);
	                Map<String, String> tokens = new HashMap<>();
	                tokens.put("access_token", access_token);
	                tokens.put("refresh_token", refresh_token);
	                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
	                break;
	            }catch (Exception exception) {
	                response.setHeader("error", exception.getMessage());
	                response.setStatus(HttpStatus.FORBIDDEN.value());
	                //response.sendError(FORBIDDEN.value());
	                Map<String, String> error = new HashMap<>();
	                error.put("error_message", exception.getMessage());
	                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
	                new ObjectMapper().writeValue(response.getOutputStream(), error);
	                break;
	            }
			} else {
				continue;
			}
		}
    }
	
	private void validateAddUser(Users user) {
		Users tempUser = userRepo.findByUsername(user.getUsername());
		if(tempUser != null) {
			throw new DataRelatedException(ERROR_CODE.USER_ALREADY_EXIST, "This username has been used.");
		}
		if( !(user.getRole().equals("ROLE_CUSTOMER") || user.getRole().equals("ROLE_STAFF") || user.getRole().equals("ROLE_ADMIN")) ) {
			throw new DataRelatedException(ERROR_CODE.INVALID_ATTRIBUTE, "Roles must be ROLE_CUSTOMER, ROLE_STAFF, or ROLE_ADMIN");
		}
		if(user.getPassword() == null) {
			throw new DataRelatedException(ERROR_CODE.INVALID_ATTRIBUTE, "Password cannot be null");
		}
	}
	
	private void validateEditUser(Users user, boolean isRoleEdit) {
		Users tempUser = userRepo.findById(user.getUid()).orElseThrow(() -> 
			new DataRelatedException(ERROR_CODE.USER_DOESNT_FOUND, "User with user id "+user.getUid()+" does not found."));
		if(!user.getUsername().equals(tempUser.getUsername())) {
			throw new DataRelatedException(ERROR_CODE.INVALID_ATTRIBUTE, "Username cannot be changed");
		}
		if( !(user.getRole().equals("ROLE_CUSTOMER") || user.getRole().equals("ROLE_STAFF") || user.getRole().equals("ROLE_ADMIN")) ) {
			throw new DataRelatedException(ERROR_CODE.INVALID_ATTRIBUTE, "Roles must be ROLE_CUSTOMER, ROLE_STAFF, or ROLE_ADMIN");
		} 
		if(!isRoleEdit) {
			if(!user.getRole().equals(tempUser.getRole())) {
				throw new DataRelatedException(ERROR_CODE.INVALID_ATTRIBUTE, "You cannot change your role!");
			}
			if(user.getPassword() == null) {
				user.setPassword(tempUser.getPassword());
			} else {
				user.setPassword( passwordEncoder.encode(user.getPassword()) );
			}
		} else {
			user.setPassword(tempUser.getPassword());
		}
		
		
	}

	public boolean checkIfUserExists(String username) {
		Users user = userRepo.findByUsername(username);
		if(user != null) {
			return true;
		}
		return false;
	}
	
	

}
