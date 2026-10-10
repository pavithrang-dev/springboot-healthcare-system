package com.healthcare.patient_service.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// PURPOSE: This is the MASTER CONTROL PANEL for all security rules.
// It defines:
//   1. Which URLs are public vs protected
//   2. Which roles can access which endpoints
//   3. That we're using JWT (stateless, no sessions)
//   4. Where our JwtAuthenticationFilter sits in the filter chain
//   5. How passwords are encrypted
@Configuration      // tells Spring: "this class creates beans (objects Spring manages)"
@EnableWebSecurity  // tells Spring: "activate the security filter chain"
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }


    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http.csrf(csrf->csrf.disable())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/api/auth/**").permitAll()       // login endpoint
                        .requestMatchers("/swagger-ui/**").permitAll()     // Swagger docs
                        .requestMatchers("/v3/api-docs/**").permitAll()    // OpenAPI JSON
                        .requestMatchers("/actuator/health").permitAll() // health check

                        .requestMatchers(HttpMethod.POST,"api/patients").hasAnyRole("RECEPTIONIST","ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/patients").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers("/api/patients/aadhaar/**").hasRole("ADMIN")
                        .requestMatchers("/api/patients/id/**").hasAnyRole("DOCTOR", "ADMIN")
                        .anyRequest().authenticated()
                )

                // ── 4. INSERT OUR JWT FILTER ──
        // WHY addFilterBefore?
        // Spring Security has a built-in UsernamePasswordAuthenticationFilter.
        // We want OUR JwtAuthenticationFilter to run BEFORE it.
        // Our filter checks the JWT → sets the SecurityContext → done.
        // If we put it AFTER, Spring's filter would reject the request first.
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ════════════════════════════════════════════
    //  PASSWORD ENCODER — how passwords are hashed
    // ════════════════════════════════════════════
    // WHY BCrypt?
    // - Has a built-in SALT (random value) — same password produces different hashes
    // - Has a COST FACTOR (the "10" in $2a$10$...) — intentionally slow
    // - "admin123" → "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"
    // - Even if database is stolen, passwords take YEARS to crack
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // ════════════════════════════════════════════
    //  AUTHENTICATION MANAGER — the "reception desk"
    // ════════════════════════════════════════════
    // WHY expose this as a bean?
    // Our AuthController needs to call authenticationManager.authenticate()
    // during login. By default, it's not accessible. This makes it injectable.
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
