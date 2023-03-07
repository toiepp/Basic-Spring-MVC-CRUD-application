package me.mikholsky.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
	@Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

		manager.createUser(User.withDefaultPasswordEncoder()
							   .username("admin")
							   .password("admin1")
							   .roles("ADMIN", "MANAGER", "EMPLOYEE")
							   .build());
		manager.createUser(User.withDefaultPasswordEncoder()
							   .username("manager")
							   .password("manager1")
							   .roles("MANAGER", "EMPLOYEE")
							   .build());
		manager.createUser(User.withDefaultPasswordEncoder()
							   .username("employee")
							   .password("employee1")
							   .roles("EMPLOYEE")
							   .build());

		return manager;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic();

		http.authorizeHttpRequests(authorize -> authorize
			.requestMatchers("/", "/authenticate/**", "/error/**", "/logging/**").permitAll()
			.requestMatchers("/customers/update-page", "/customers/delete", "/customers/update").hasRole("ADMIN")
			.requestMatchers("/customers/all-page").authenticated()
			.requestMatchers("/customers/**").hasAnyRole("ADMIN", "MANAGER")
		);

		http.formLogin(formLogin -> formLogin
			.loginPage("/authenticate/login")
			.loginProcessingUrl("/authenticate/login/process")
			.failureUrl("/authenticate/login?failure")
			.permitAll()/*указываем это, потому что все должны иметь доступ к странице авторизации*/);

		http.logout()
			.logoutUrl("/authenticate/login?logout")
			.logoutSuccessUrl("/authenticate/login?logout");

		return http.build();
	}
}