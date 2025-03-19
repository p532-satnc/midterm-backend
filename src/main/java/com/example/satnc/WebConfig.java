package com.example.satnc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/**") // Allow CORS for all endpoints
//				.allowedOrigins("http://localhost:8000") // Allow frontend to access
//				.allowedMethods("GET", "POST", "PUT", "DELETE") // Allow HTTP methods
//				.allowedHeaders("*"); // Allow all headers
//	}
//}


@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// Allow requests from your GitHub Pages domain
		registry.addMapping("/**")
				.allowedOrigins("https://p532-satnc.github.io") // Add your GitHub Pages URL here
				.allowedMethods("GET", "POST", "PUT", "DELETE") // Specify allowed HTTP methods
				.allowedHeaders("*")
				.allowCredentials(true);
	}
}
