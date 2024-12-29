package com.honestefforts.fixengine.service.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.honestefforts.fixengine.service.security.jwt.AccessTokenAuthenticationFailureHandler;
import com.honestefforts.fixengine.service.security.jwt.AccessTokenFilter;
import com.honestefforts.fixengine.service.security.jwt.utility.JwtTokenValidator;

@Order(1)
@EnableWebSecurity
@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf((csrf) -> csrf.disable())
			.authorizeHttpRequests(
					(authorize) -> authorize.requestMatchers(
							new AntPathRequestMatcher("v1/health",
									HttpMethod.GET.toString()),
								new AntPathRequestMatcher("/swagger-ui/index.html", 
										HttpMethod.GET.toString()),
								new AntPathRequestMatcher("/swagger-ui/**", 
                                        HttpMethod.GET.toString()),
								new AntPathRequestMatcher("/v3/api-docs/**"),
								new AntPathRequestMatcher("/v1/oauth2.0/cached/token"))
					.permitAll().anyRequest().authenticated())
					.securityMatchers((securityMatch) -> 
						securityMatch.requestMatchers(
								new AntPathRequestMatcher("v1/processFixMessages", 
										HttpMethod.POST.toString())))
					.addFilterBefore(new AccessTokenFilter(new JwtTokenValidator(), 
							authenticationFailureHandler()), 
							BasicAuthenticationFilter.class)
					.sessionManagement((sessionManage) -> sessionManage
							.sessionCreationPolicy(
									SessionCreationPolicy.STATELESS));
		
		return http.build();
	}
	
	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new AccessTokenAuthenticationFailureHandler();
	}
}
