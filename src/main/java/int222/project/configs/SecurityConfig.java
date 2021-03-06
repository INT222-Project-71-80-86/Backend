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
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import int222.project.filter.CorsFilter;
import int222.project.filter.CustomAuthenticationFilter;
import int222.project.filter.CustomAuthorizationFilter;
import int222.project.services.MyUserServices;
import lombok.RequiredArgsConstructor;

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	private final MyUserServices userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	@Value("${jwt.token.secret}") private String secret;
	@Value("${server.fe.ip.addr}")private String host;
	
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
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/get", "/api/order/get/id/*", "/api/order/get/username", "/api/coupon/get", "/api/coupon/check").hasAnyAuthority("ROLE_CUSTOMER","ROLE_STAFF","ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/allusers","/api/coupon/allcoupons","/api/orders", 
				"/api/orderdetails", "/api/reviews", "/api/prodcolors").hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/file/", "/api/order/all").hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/product/**", "/api/product", "/api/brand", "/api/brand/*",
				"/api/color", "/api/color/*", "/api/cats", "/api/file/*", "/api/review/*", "/api/user/check/*").permitAll();
		/* ----------------------POST-------------------- */
		http.authorizeRequests().antMatchers(HttpMethod.POST,  "/api/user/save").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/brand/save", "/api/color/save", "/api/coupon/save").hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/product/save").hasAnyAuthority("ROLE_STAFF","ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/something").hasAnyAuthority("ROLE_STAFF");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/review/save", "/api/order/save").hasAnyAuthority("ROLE_CUSTOMER");
		/* ----------------------PUT--------------------- */
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/brand/edit", "/api/color/edit", "/api/user/roleedit", "/api/coupon/edit").hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/product/edit").hasAnyAuthority("ROLE_STAFF","ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/user/edit").hasAnyAuthority("ROLE_STAFF","ROLE_ADMIN","ROLE_CUSTOMER");
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/review/edit").hasAnyAuthority("ROLE_CUSTOMER");
		/* ---------------------DELETE------------------- */
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/brand/delete/*", "/api/color/delete/*", "/api/coupon/delete/*").hasAnyAuthority("ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/product/delete/*").hasAnyAuthority("ROLE_STAFF","ROLE_ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/review/delete", "/api/order/cancel/*").hasAnyAuthority("ROLE_CUSTOMER","ROLE_STAFF","ROLE_ADMIN");
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(secret), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new CorsFilter(host), ChannelProcessingFilter.class);
	}
	
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
