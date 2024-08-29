package com.orionsolution.oauthsecurity.config;

import com.orionsolution.oauthsecurity.entity.ApplicationEntity;
import com.orionsolution.oauthsecurity.repository.ApplicationRepository;
import com.orionsolution.oauthsecurity.utility.ApplicationKeyUtility;
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
import java.util.Optional;

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
          HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
    String authorizationHeader = request.getHeader("App-Key-Header");
    filterRequest(request, response, filterChain, authorizationHeader);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }

  private void filterRequest(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String authorizationHeader) {
    if (authorizationHeader != null) {
      Optional<ApplicationEntity> application = applicationRepository.findByApplicationId(authorizationHeader);
      application.ifPresentOrElse(app -> {
        if (authorizationHeader.equals(app.getApplicationId())) {
          log.info("## LOGGED WITH APP {} ##", app.getApplicationName());
          ApplicationKeyUtility.setAppKey(authorizationHeader);
          try {
            filterChain.doFilter(request, response);
          } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
          }
        }
      }, () -> response.setStatus(HttpServletResponse.SC_UNAUTHORIZED));
    }
  }

}
