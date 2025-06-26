package com.example.dws.security;


import com.example.dws.security.jwt.JwtRequestFilter;
import com.example.dws.security.jwt.UnauthorizedHandlerJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	RepositoryUserDetailsService userDetailsService;

	@Autowired
	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());
		
		http
			.securityMatcher("/api/**")
			.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

		http
				.authorizeHttpRequests(authorize -> authorize

						.requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/shops/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/users").permitAll()

						.requestMatchers(HttpMethod.POST, "/api/products/users/me/products/**").hasRole("USER")
						.requestMatchers(HttpMethod.GET, "/api/users/users/me/cart").hasRole("USER")
						.requestMatchers(HttpMethod.GET, "/api/users/users/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers(HttpMethod.POST, "/api/comments/**").hasRole("USER")
						.requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")

						.requestMatchers(HttpMethod.DELETE, "/api/comments/**").hasAnyRole("ADMIN", "USER")
						.requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/shops/**").hasAnyRole("ADMIN", "USER")
						.requestMatchers(HttpMethod.POST, "/api/shops/comments/*").hasAnyRole("ADMIN", "USER")

						.requestMatchers(HttpMethod.POST, "/api/products/{productID}/shops/{shopID}").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/api/shops/**").hasRole( "ADMIN")
						.requestMatchers(HttpMethod.POST, "/api/shops/{shopID}/image").hasRole("USER")
						.requestMatchers(HttpMethod.POST, "/comments/{shopID}").hasAnyRole("ADMIN", "USER")

						.anyRequest().permitAll()
				);
        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http
				.authorizeHttpRequests(authorize -> authorize
						// PUBLIC PAGES
						.requestMatchers("/").permitAll()
						.requestMatchers("/shopImage/**").permitAll()
						.requestMatchers("/shops/**").permitAll()
						.requestMatchers("/error").permitAll()
						.requestMatchers("/index").permitAll()
						.requestMatchers("/styles.css").permitAll()
						.requestMatchers("/favicon.ico").permitAll()
						.requestMatchers("/products/**").permitAll()
						.requestMatchers("/register/**").permitAll()
						.requestMatchers("/js/**").permitAll()
						.requestMatchers("/comments/**").permitAll()
						.requestMatchers("/login").permitAll()
						.requestMatchers("/productSearch").permitAll()
						.requestMatchers("/comments/{shopId}/{commentId}").permitAll()
						.requestMatchers("/products/{productID}").permitAll()
						// PRIVATE PAGES
						.requestMatchers("/profile").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/save").hasAnyRole("ADMIN")
						.requestMatchers("/users/{userID}/**").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/users/**").hasAnyRole("ADMIN")
						.requestMatchers("/users/getLogged/").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/shops/{shopID}/products/new").hasAnyRole("ADMIN")
						.requestMatchers("/shops/{shopID}/delete").hasAnyRole("ADMIN")
						.requestMatchers("/shops/{shopID}/comments/new").hasAnyRole("ADMIN")
						.requestMatchers("/comments/{shopId}/{commentId}/deleteComment").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/products/{productID}/addShop/").hasAnyRole("ADMIN")
						.requestMatchers("/products/{productID}/delete").hasAnyRole("ADMIN")
						.requestMatchers("/products/{productID}/buy").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/products/{productId}/update/").hasAnyRole("ADMIN")

				)
				.formLogin(formLogin -> formLogin
						.loginPage("/login")
						.failureUrl("/loginerror")
						.defaultSuccessUrl("/index")
						.permitAll()
				)
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.permitAll()
				);

		return http.build();
	}
}
