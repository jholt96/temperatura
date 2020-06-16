package edge.temperatura.temperatura.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final PasswordEncoder passwordEncoder;

	@Autowired
	public SecurityConfig(PasswordEncoder passwordEncoder){
		this.passwordEncoder = passwordEncoder;
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/management/api/**").hasAnyRole("ADMIN","VIEWER")
				.antMatchers("/api/**").hasAnyRole("ADMIN","VIEWER")
				.anyRequest().authenticated()
				.and()
				.httpBasic();
	}

	@Override
	@Bean
	protected UserDetailsService userDetailsService(){
		UserDetails jishUser = User.builder()
								.username("jish")
								.password(passwordEncoder.encode("password"))
								.roles("ADMIN")
								.build();

		UserDetails marissaUser = User.builder()
								.username("marissa")
								.password(passwordEncoder.encode("password"))
								.roles("VIEWER")
								.build();

		return new InMemoryUserDetailsManager(jishUser, marissaUser);
	}
}