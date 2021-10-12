package int222.project.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
