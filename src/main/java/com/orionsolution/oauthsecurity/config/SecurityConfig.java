package com.orionsolution.oauthsecurity.config;

import com.orionsolution.oauthsecurity.entity.ApplicationEntity;
import com.orionsolution.oauthsecurity.repository.ApplicationRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@Slf4j
@EnableWebSecurity
public class SecurityConfig extends OncePerRequestFilter {

  private final ApplicationRepository applicationRepository;

  public SecurityConfig(ApplicationRepository applicationRepository) {
    this.applicationRepository = applicationRepository;
  }

  @Bean
  public SecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception {
    return httpSecurity.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
        .build();
  }

  @Override
  protected void doFilterInternal(
          HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
    String authorizationHeader = request.getHeader("App-Key-Header");
    if (authorizationHeader != null) {
      ApplicationEntity application = applicationRepository.findByHashId(authorizationHeader);
      if (authorizationHeader.equals(application.getHashId())) {
        log.info("## LOGGED WITH APP {} ##", application.getApplicationName());
        filterChain.doFilter(request, response);
        return;
      }
    }
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }
}
