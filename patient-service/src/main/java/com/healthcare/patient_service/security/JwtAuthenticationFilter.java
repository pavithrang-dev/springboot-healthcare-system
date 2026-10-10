package com.healthcare.patient_service.security;



import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

// PURPOSE: This filter runs ONCE for EVERY request (OncePerRequestFilter).
// It's the GUARD that checks: "Do you have a valid JWT in your Authorization header?"
//
// WHY do we need a CUSTOM filter?
// Spring Security doesn't have a built-in filter for "our custom JWT" out of the box.
// It has BearerTokenAuthenticationFilter for OAuth2 resource servers,
// but since we're issuing our OWN JWTs (not from Keycloak/Auth0), we write our own filter.
//
// WHAT DOES IT DO?
// 1. Look at the Authorization header
// 2. If it starts with "Bearer ", extract the token
// 3. Validate the token (signature + expiry)
// 4. If valid, load the user and put them in SecurityContextHolder (the "wristband")
// 5. If invalid or missing, do nothing — let the next filter handle it (will result in 401)

@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter  {
    private final JwtService jwtService;
    private CustomUserDetailService userDetailService;
    public JwtAuthenticationFilter(JwtService jwtService,CustomUserDetailService userDetailService) {
        this.jwtService = jwtService;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // ── Step 1: Get the Authorization header ──
        final String authHeader = request.getHeader("Authorization");
        // If no header or doesn't start with "Bearer " → skip this filter
        // WHY skip instead of reject?
        // Some endpoints are PUBLIC (like /api/auth/login, /swagger-ui).
        // We let them through here. The AuthorizationFilter later decides
        // if the endpoint actually requires authentication.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);  // pass to next guard
            return;
        }
        // ── Step 2: Extract the token (remove "Bearer " prefix) ──
        // "Bearer eyJhbGciO..." → "eyJhbGciO..."
        final String jwt = authHeader.substring(7);

        // ── Step 3: Extract username from token ──
        final String username = jwtService.extractUsername(jwt);

        // ── Step 4: If we got a username AND user is not already authenticated ──
        // WHY check SecurityContextHolder?
        // If a previous filter already authenticated this request,
        // we don't need to do it again. Avoid duplicate work.
        if (username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            // ── Step 5: Load user from database ──
            UserDetails userDetails = userDetailService.loadUserByUsername(username);

            // ── Step 6: Validate the token ──
            if (jwtService.isTokenValid(jwt)) {

                // ── Step 7: Create the "wristband" (Authentication object) ──
                // This tells Spring Security: "This person is verified"
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,           // WHO: the user
                                null,                  // credentials: null (already verified via JWT)
                                userDetails.getAuthorities()  // WHAT: their roles
                        );

                // Attach request details (IP address, session ID) for audit logging
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                // ── Step 8: Put the wristband in SecurityContextHolder ──
                // From this point, ANY code in your app can call:
                //   SecurityContextHolder.getContext().getAuthentication()
                // to know WHO the current user is.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // ── Step 9: Pass to the next filter in the chain ──
        // This is CRITICAL. If you forget this line, the request stops here
        // and never reaches your controller. The chain is broken.
        filterChain.doFilter(request, response);

        }
    }

