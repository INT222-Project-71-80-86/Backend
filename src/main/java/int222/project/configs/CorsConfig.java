package int222.project.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
	
	@Value("${server.fe.ip.addr}")private String host;
	@Value("${server.fe.port}")private String port;

	// Add Configuration Method to allow CORS Policy
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(host+":"+port).allowedMethods("*");
    }
}