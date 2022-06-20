package sk.posam;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class PosamApp {
	public static void main(String[] args) {
		SpringApplication.run(PosamApp.class, args);
	}

	@Bean
	public CommandLineRunner demo(DataSource dataSource) {
		return (args) -> {
			System.out.println(dataSource);
		};
	}

	/*@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().and().cors().disable()
					.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
					.authorizeRequests()
					.antMatchers(HttpMethod.POST, "/api/home").permitAll()
					.anyRequest().authenticated();
		}
	}*/

}
