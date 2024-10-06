package com.inlaco.crewmgrservice.config;

import com.inlaco.crewmgrservice.feature.auth.jwt.AuthEntryPointJwt;
import com.inlaco.crewmgrservice.feature.auth.jwt.LazyJwtAuthTokenFilter;
import com.inlaco.crewmgrservice.utils.ApiEndpointSecurityInspector;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
  private final LazyJwtAuthTokenFilter lazyJwtAuthTokenFilter;
  // private final LogoutHandler logoutHandler;
  //
  private final UserDetailsService userDetailsService;
  private final AuthEntryPointJwt unauthorizedHandler;
  private final ApiEndpointSecurityInspector apiEndpointSecurityInspector;
  private final LogoutSuccessHandler logoutSuccessHandler;
  private final LogoutHandler logoutHandler;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(List<AuthenticationProvider> providers) {
    return new ProviderManager(providers);
  }

  // @Bean
  // public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
  //     throws Exception {
  //   return authConfig.getAuthenticationManager();
  // }

  @Bean
  public CorsConfigurationSource corsApiConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:*", "*.ngrok-free.app"));
    // configuration.addAllowedOriginPattern("*");
    // configuration.addAllowedHeader("*");
    // configuration.addAllowedMethod("*");
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    configuration.setAllowedMethods(
        List.of(
            "HEAD", "GET", "POST", "PUT", "DELETE", "PATCH",
            "OPTIONS")); // <-- methods allowed in CORS policy
    configuration.setAllowedHeaders(
        List.of(
            "Authorization",
            "Cache-Control",
            "Content-Type",
            "Accept",
            "X-Api-Key",
            "X-Forwarded-For",
            "X-Requested-With",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Headers",
            "Origin")); // <-- headers allowed in CORS policy
    configuration.setExposedHeaders(
        List.of(
            "Authorization",
            "Cache-Control",
            "Content-Type",
            "Accept",
            "X-Requested-With",
            "X-Rate-Limit-Retry-After-Seconds",
            "X-Rate-Limit-Remaining",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Headers",
            "Origin")); // <-- headers exposed in CORS policy
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    apiEndpointSecurityInspector.getPublicEndpoints().add("/api/v1/auth/**");
    apiEndpointSecurityInspector.getPublicEndpoints().add("/actuator/**");

    http.cors(cors -> cors.configurationSource(corsApiConfigurationSource()))
        // .csrf((csrf) -> csrf.disable())
        .csrf(
            customizer -> {
              // customizer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
              customizer.ignoringRequestMatchers("/**", "/actuator/**");
            })

        // exception handling
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))

        // session management
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        // authorize
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        apiEndpointSecurityInspector.getPublicEndpoints().toArray(String[]::new))
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.GET,
                        apiEndpointSecurityInspector.getPublicGetEndpoints().toArray(String[]::new))
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.POST,
                        apiEndpointSecurityInspector
                            .getPublicPostEndpoints()
                            .toArray(String[]::new))
                    .permitAll()
                    .requestMatchers(HttpMethod.PATCH)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(lazyJwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(
            logout ->
                logout
                    .logoutUrl("/api/v1/auth/logout")
                    .addLogoutHandler(logoutHandler)
                    .logoutSuccessHandler(logoutSuccessHandler));

    return http.build();
  }
}
