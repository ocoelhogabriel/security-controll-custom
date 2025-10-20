package com.ocoelhogabriel.security_control_custom.infrastructure.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ocoelhogabriel.security_control_custom.infrastructure.exception.CustomAccessDeniedHandler;
import com.ocoelhogabriel.security_control_custom.infrastructure.exception.CustomAuthenticationEntryPoint;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@EnableWebSecurity
@SecurityScheme(name = "bearerAuth", description = "JWT Authentication", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class SecurityConfig {

//	@Autowired
//	@Lazy
//	private JWTAuthFilter authFilter;

	private static final String[] WHITE_LIST_URL = { "/api/medicao/v1/criarMedicao/**", "/api/medicao/v2",
			"/api/modulo-device/v1/keepAlive/**", "/api/modulo-device/v1/auth-validate", "/api/modulo-device/v1/auth",
			"/api/autenticacao/v1/**", "/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**", "/swagger-resources",
			"/swagger-resources/**", "/configuration/ui", "/configuration/security", "/swagger-ui/**", "/webjars/**",
			"/swagger-ui.html", "/swagger-ui/index.html" };

	@Bean
	SecurityFilterChain filterChain(JWTAuthFilter authFilter, HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				// Filtro de requisição
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
				.authorizeHttpRequests(requests -> requests
						// list white request
						.requestMatchers(WHITE_LIST_URL).permitAll()
						// .anyRequest().permitAll())
						.anyRequest().authenticated())
				// Sessão
				.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
				// Lidar com Exceções
				.exceptionHandling(ex -> ex
						// Acesso negado
						.accessDeniedHandler(new CustomAccessDeniedHandler())
						// Acesso negado por endpoint
						.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
