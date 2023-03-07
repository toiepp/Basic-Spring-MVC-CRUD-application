package me.mikholsky.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.PasswordManagementConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.function.Function;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
	private DataSource dataSource;

	private Environment environment;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Autowired
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(
			Objects.requireNonNull(
				environment.getProperty("spring.security.bcrypt.strength", Integer.class)));
	}

	@Bean
	public UserDetailsManager jdbcUserDetails() {
		var jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

		jdbcUserDetailsManager
			.createUser(User.builder()
							.passwordEncoder(passwordEncoder()::encode)
							.username("admin")
							.password("admin1")
							.roles("ADMIN", "MANAGER", "EMPLOYEE")
							.build());

		return new JdbcUserDetailsManager(dataSource);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
			.requestMatchers("/", "/authenticate/**", "/error/**", "/logging/**").permitAll()
			.requestMatchers("/customers/update-page", "/customers/delete", "/customers/update").hasRole("ADMIN")
			.requestMatchers("/customers/all-page").authenticated()
			.requestMatchers("/customers/**").hasAnyRole("ADMIN", "MANAGER")
			.and()
			.formLogin()
			.loginPage("/authenticate/login")
			.loginProcessingUrl("/authenticate/process")
			.failureUrl("/authenticate/login?failure")
			.and()
			.logout()
			.logoutUrl("/authenticate/login?logout")
			.logoutSuccessUrl("/authenticate/login?logout");

		return http.build();
	}
}