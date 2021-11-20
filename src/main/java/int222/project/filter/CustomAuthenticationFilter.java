package int222.project.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final String secret;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, String secret) {
        this.authenticationManager = authenticationManager;
        this.secret = secret;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username+" "+password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }
    
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//    		AuthenticationException failed) throws IOException, ServletException {
//    	
//    	response.setHeader("error", "Unsuccessful Authentication");
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        Map<String, String> error = new HashMap<>();
//        error.put("error_message", "Unsuccessful Authentication");
//        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
//        new ObjectMapper().writeValue(response.getOutputStream(), error);
////    	response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unsuccessful Authentication");
//    	super.unsuccessfulAuthentication(request, response, failed);
//    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user = (User)authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 6 * 60 * 60 * 1000)) //30 * 60 * 1000
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000)) //12 * 60 * 60 * 1000
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        /*response.setHeader("access_token", access_token);
        response.setHeader("refresh_token", refresh_token);*/
        
//        Cookie cookie = new Cookie("refresh_token", refresh_token);
//        cookie.setHttpOnly(true);
//        cookie.setMaxAge(12*60*60);
//        cookie.setPath("/");
//        cookie.setDomain("localhost");
//        response.addCookie(cookie);
        
        ResponseCookie resCookie = ResponseCookie.from("refresh_token", refresh_token)
        		.httpOnly(true)
        		.secure(true)
        		.sameSite("None")
        		.path("/")
        		.domain("")
        		.maxAge(12*60*60)
        		.build();
        response.setHeader(HttpHeaders.SET_COOKIE, resCookie.toString());
        
//        cookie.setSecure(true);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
//        tokens.put("username", user.getUsername());
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
