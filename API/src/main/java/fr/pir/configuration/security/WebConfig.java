package fr.pir.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fr.pir.config.RateLimitingInterceptor;

/**
 * Web configuration including CORS settings and rate limiting
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	private static final Logger L = LogManager.getLogger(WebConfig.class);

	@Value("${cors.allowed.origins}")
	private String allowedOrigins;

	@Value("${cors.allowed.methods:GET,POST,PUT,PATCH,DELETE,OPTIONS}")
	private String allowedMethods;

	@Value("${cors.allowed.headers:Authorization,Content-Type,Accept}")
	private String allowedHeaders;

	@Autowired
	private RateLimitingInterceptor rateLimitingInterceptor;

	/**
	 * Enable CORS for all endpoints with configuration from properties
	 *
	 * @param registry -> CorsRegistry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		L.info("Configuring CORS with origins: {}", allowedOrigins);

		// Warn if wildcard is used
		if (allowedOrigins.equals("*")) {
			L.warn("⚠️ WARNING: CORS is set to wildcard (*). This is not recommended for production!");
		}

		String[] origins = allowedOrigins.split(",");
		String[] methods = allowedMethods.split(",");
		String[] headers = allowedHeaders.split(",");

		registry.addMapping("/**")
			.allowedOriginPatterns("*") // Use allowedOriginPatterns instead of allowedOrigins to support wildcards with credentials
			.allowedMethods(methods)
			.allowedHeaders(headers)
			.allowCredentials(true)
			.maxAge(3600);

		L.info("CORS configured successfully with pattern *");
	}

	/**
	 * Register rate limiting interceptor
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		L.info("Registering rate limiting interceptor");
		registry.addInterceptor(rateLimitingInterceptor)
			.addPathPatterns("/api/auth/**");
	}

}
