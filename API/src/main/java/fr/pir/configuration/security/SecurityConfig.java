package fr.pir.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.pir.service.security.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger L = LogManager.getLogger(SecurityConfig.class);

    @Autowired
    private AuthenticationEntryPointConfig authenticationEntryPointConfig;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthRequestMatcher authRequestMatcher;

    @Autowired
    private RequestFilterConfig requestFilterConfig;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        L.debug("");

        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        L.debug("");

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(this.customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        L.debug("");

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll() // Explicitly permit all auth endpoints
                        .requestMatchers(this.authRequestMatcher.getRequestMatchers())
                        .permitAll().anyRequest().authenticated())
                .exceptionHandling(exce -> exce.authenticationEntryPoint(this.authenticationEntryPointConfig))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(this.requestFilterConfig, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        L.debug("");

        return authenticationConfiguration.getAuthenticationManager();
    }

}
