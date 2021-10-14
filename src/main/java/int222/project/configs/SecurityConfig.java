package int222.project.configs;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import int222.project.filter.CustomAuthenticationFilter;
import int222.project.filter.CustomAuthorizationFilter;
import int222.project.services.MyUserServices;
import lombok.RequiredArgsConstructor;

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	private final MyUserServices userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	@Value("${jwt.token.secret}") private String secret;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(),secret);
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(STATELESS);
		http.authorizeRequests().antMatchers("/api/login/**", "/api/user/token/refresh").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/get/*").hasAnyAuthority("ROLE_CUSTOMER","ROLE_STAFF","ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/file", "/api/user/allusers").hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/product/**", "/api/product", "/api/brand", "/api/brand/*",
				"/api/color", "/api/color/*", "/api/cats", "/api/file/*", "/api/review/*").permitAll();
		/* ----------------------POST-------------------- */
		http.authorizeRequests().antMatchers(HttpMethod.POST,  "/api/user/save").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/brand/save", "/api/color/save").hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/product/save").hasAnyAuthority("ROLE_STAFF","ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/something").hasAnyAuthority("ROLE_STAFF");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/review/save").hasAnyAuthority("ROLE_CUSTOMER");
		/* ----------------------PUT--------------------- */
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/brand/edit", "/api/color/edit", "/api/user/edit").hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/product/edit").hasAnyAuthority("ROLE_STAFF","ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/something").hasAnyAuthority("ROLE_STAFF");
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/review/edit").hasAnyAuthority("ROLE_CUSTOMER");
		/* ---------------------DELETE------------------- */
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/brand/delete/*", "/api/color/delete/*").hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/product/delete/*").hasAnyAuthority("ROLE_STAFF","ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/something").hasAnyAuthority("ROLE_STAFF");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/something").hasAnyAuthority("ROLE_CUSTOMER");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/review/delete/*").hasAnyAuthority("ROLE_CUSTOMER","ROLE_STAFF","ROLE_ADMIN");
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(secret), UsernamePasswordAuthenticationFilter.class);
	}
	
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
