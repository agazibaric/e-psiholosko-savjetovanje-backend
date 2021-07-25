package com.epsih.config;

import com.epsih.constants.AuthorityConstants;
import com.epsih.constants.Endpoints;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import com.epsih.security.JwtAccessDeniedHandler;
import com.epsih.security.JwtAuthenticationEntryPoint;
import com.epsih.security.jwt.JWTConfigurer;
import com.epsih.security.jwt.TokenProvider;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

   private final TokenProvider tokenProvider;
   private final CorsFilter corsFilter;
   private final JwtAuthenticationEntryPoint authenticationErrorHandler;
   private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

   public WebSecurityConfig(
      TokenProvider tokenProvider,
      CorsFilter corsFilter,
      JwtAuthenticationEntryPoint authenticationErrorHandler,
      JwtAccessDeniedHandler jwtAccessDeniedHandler
   ) {
      this.tokenProvider = tokenProvider;
      this.corsFilter = corsFilter;
      this.authenticationErrorHandler = authenticationErrorHandler;
      this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Override
   public void configure(WebSecurity web) {
      web.ignoring()
         .antMatchers(HttpMethod.OPTIONS, "/**")

         // allow anonymous resource requests
         .antMatchers(
            "/",
            "/*.html",
            "/favicon.ico",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js",
            "/h2-console/**"
         );
   }

   @Override
   protected void configure(HttpSecurity httpSecurity) throws Exception {
      httpSecurity
         // We don't need CSRF because our token is invulnerable
         .csrf().disable()

         .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

         .exceptionHandling()
         .authenticationEntryPoint(authenticationErrorHandler)
         .accessDeniedHandler(jwtAccessDeniedHandler)

         // Enable h2-console
         .and()
         .headers()
         .frameOptions()
         .sameOrigin()

         // Create no session
         .and()
         .sessionManagement()
         .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

         .and()
         .authorizeRequests()

         // Swagger
         .antMatchers("/v3/**").permitAll()

         // Auth endpoints
         .antMatchers("/api/authenticate").permitAll()
         .antMatchers("/api/register").permitAll()
         .antMatchers("/api/activate/*").permitAll()

         .antMatchers("/api/admin").hasAuthority(AuthorityConstants.ROLE_ADMIN)
         .antMatchers(Endpoints.AUTH_REGISTER_DOCTOR).hasAuthority(AuthorityConstants.ROLE_ADMIN)

         .antMatchers(HttpMethod.GET, "/api/category").permitAll()
         .antMatchers(HttpMethod.GET, "/api/service").permitAll()
         .antMatchers("/ws/**").permitAll()

         .antMatchers(Endpoints.PATIENT_ROOT).hasAuthority(AuthorityConstants.ROLE_USER)
         .antMatchers(Endpoints.PATIENT_ROOT + "/**").hasAuthority(AuthorityConstants.ROLE_USER)
         .antMatchers(Endpoints.DOCTOR_ROOT).hasAuthority(AuthorityConstants.ROLE_DOCTOR)
         .antMatchers(Endpoints.DOCTOR_ROOT + "/**").hasAuthority(AuthorityConstants.ROLE_DOCTOR)

         .anyRequest().authenticated()

         .and()
         .apply(securityConfigurerAdapter());
   }

   private JWTConfigurer securityConfigurerAdapter() {
      return new JWTConfigurer(tokenProvider);
   }

}
