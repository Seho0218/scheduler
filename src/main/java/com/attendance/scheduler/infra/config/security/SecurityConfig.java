package com.attendance.scheduler.infra.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{
	public static final String[] ENDPOINTS_WHITELIST = {
			"/", "/submit", "/completion",
			"/class/**",
			"/board/**",
			"/join/**",
			"/login/**",
			"/cert/**",
			"/help/**",
			"/comment/**",
			"/css/**",
			"/js/**"
	};

	@Bean
	public static PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain teacherFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.csrf().disable()
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(ENDPOINTS_WHITELIST).permitAll()
						.requestMatchers("/login/Login").permitAll()
						.requestMatchers("/manage/*").hasAnyRole("TEACHER", "ADMIN")
						.requestMatchers("/admin/*").hasRole("ADMIN")
						.anyRequest().authenticated()
				)
				.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/login/Login")
				.defaultSuccessUrl("/manage/class", true)
				.failureHandler(new CustomAuthenticationFailureHandler())
				.and()
				.logout()
				.logoutUrl("/logout")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.logoutSuccessUrl("/");
		log.info("1st Configurer");
		return httpSecurity.build();
	}
}