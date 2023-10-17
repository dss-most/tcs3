package tcs3.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import tcs3.auth.service.DssAuthenticationProvider;
import tcs3.auth.service.DssUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private DssAuthenticationProvider authenticationProvider;

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder builder =
			http.getSharedObject(AuthenticationManagerBuilder.class);
		builder.authenticationProvider(authenticationProvider);
		return builder.build();
	}

	// @Autowired
	// public void configureGlobal(AuthenticationManagerBuilder auth)
	// 		throws Exception {
	// 	auth
	// 		.authenticationProvider(authenticationProvider)
	// 		.userDetailsService(userDetailsService);
	// }

	@Bean
	public UserDetailsService userDetailsService() {
		return new DssUserDetailsService();
	}	


	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(Customizer.withDefaults())
			.csrf(csrf -> csrf.disable())
			.authorizeRequests( authz -> authz
				.antMatchers("/REST/TestProduct/q**").permitAll()
				.antMatchers("/REST/TestProductCategory/").permitAll()
				.antMatchers("/static/**").permitAll()
				.antMatchers("/webjars/**").permitAll()
				.antMatchers("/report/**").permitAll()
				.antMatchers("/login**").permitAll()
				.anyRequest().fullyAuthenticated())
			.formLogin(f -> f
				.loginPage("/login").permitAll()
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/"))
			.logout(logout -> logout
				.logoutSuccessUrl("/login?logout_successful=1")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")));

			// .csrf().disable()
			// .authorizeRequests()
			// 	.antMatchers("/REST/TestProduct/q**").permitAll()
			// 	.antMatchers("/REST/TestProductCategory/").permitAll()
			// 	.antMatchers("/static/**").permitAll()
			// 	.antMatchers("/webjars/**").permitAll()
			// 	.antMatchers("/report/**").permitAll()
			// 	.antMatchers("/login**").permitAll()
			// 	.anyRequest().fullyAuthenticated()
			// .and()
			// .formLogin()
			// 	.loginPage("/login").permitAll()
			// 	.loginProcessingUrl("/login")
			// 	.defaultSuccessUrl("/")
			// .and()
			// .logout()
			// 	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			// 	.logoutSuccessUrl("/login?logout_successful=1");

			return http.build();
	}
	
}