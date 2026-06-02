package com.inlaco.crewmgrservice.infrastructure.security.config;

import com.inlaco.crewmgrservice.feature.apikey.infrastructure.config.ApiKeyConfig;
import com.inlaco.crewmgrservice.feature.apikey.infrastructure.security.ApiKeyAuthenticationFilter;
import com.inlaco.crewmgrservice.infrastructure.security.jwt.filter.JwtAuthenticationFilter;
import com.inlaco.crewmgrservice.infrastructure.websocket.config.WebSocketProperties;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

  private final SecurityProperties securityProperties;
  private final WebSocketProperties webSocketProperties;
  private final ApiKeyConfig config;

  private final String[] SECURITY_WHITELIST_PATHS = {
    "/actuator/**", "/swagger-ui/**", "/v3/api-docs/**", "/scalar/**", "/webjars/**",
  };

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  // @Bean
  // public AuthenticationManager authenticationManager(List<AuthenticationProvider> providers)
  //     throws Exception {
  //   return new ProviderManager(providers);
  // }
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }

  // Request
  //  ↓
  // LazyJwtAuthTokenFilter
  //   ├─ Have JWT → authenticate
  //   └─ No JWT → skip
  //  ↓
  // AuthorizationFilter
  //  ↓
  // ApiEndpointAuthorizationManager
  //   ├─ PUBLIC → allow
  //   ├─ OPTIONAL JWT → allow
  //   └─ AUTH → require Authentication
  //  ↓
  // Controller
  //

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      JwtAuthenticationFilter lazyJwtAuthTokenFilter,
      ApiKeyAuthenticationFilter apiKeyAuthenticationFilter,
      // LogoutHandler logoutHandler,
      // LogoutSuccessHandler logoutSuccessHandler,
      AuthorizationManager authzManager,
      AuthenticationEntryPoint authenticationEntryPoint,
      UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder)
      throws Exception {
    http.cors(cors -> cors.configurationSource(corsApiConfigurationSource()))
        .csrf(
            customizer -> {
              customizer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
              customizer.ignoringRequestMatchers("/**");
            })
        // exception handling
        .exceptionHandling(
            exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
        // authorize
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                    .requestMatchers(webSocketProperties.getEndpoint() + "/**")
                    .permitAll()
                    .requestMatchers(SECURITY_WHITELIST_PATHS)
                    .permitAll()
                    .anyRequest()
                    .access(authzManager))
        .authenticationProvider(authenticationProvider(passwordEncoder, userDetailsService))
        // API Key filter must run BEFORE JWT filter
        // This allows requests with API keys to be authenticated without JWT
        .addFilterBefore(lazyJwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(apiKeyAuthenticationFilter, JwtAuthenticationFilter.class)
        // disable login and logout because we use JWT
        // session management
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .formLogin(login -> login.disable())
        // .logout(
        //     logout ->
        //         logout
        //             .logoutUrl("/api/v1/auth/logout")
        //             .addLogoutHandler(logoutHandler)
        //             .logoutSuccessHandler(logoutSuccessHandler));
        .logout(logout -> logout.disable());

    return http.build();
  }

  private DaoAuthenticationProvider authenticationProvider(
      PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }

  private CorsConfigurationSource corsApiConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOriginPatterns(securityProperties.getAllowedCorsOrigins());
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
            config.getHeaders().getKeyId(),
            config.getHeaders().getKeySecret(),
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
}
