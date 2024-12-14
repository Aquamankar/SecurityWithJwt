package com.springSecurity.stepsForSecurity.utills;

import com.springSecurity.stepsForSecurity.entity.TokenStore;
import com.springSecurity.stepsForSecurity.repositoty.TokenStoreRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final TokenStoreRepository tokenStoreRepository; // Repository to check token revocation

    public JwtAuthenticationFilter(JwtUtils jwtUtils, TokenStoreRepository tokenStoreRepository) {
        this.jwtUtils = jwtUtils;
        this.tokenStoreRepository = tokenStoreRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extract token

            // Check if the token is revoked
            if (tokenStoreRepository.findByToken(token).map(TokenStore::isRevoked).orElse(false)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has been revoked");
                return;
            }

            if (jwtUtils.validateToken(token)) {
                // Extract username and roles from the token
                String username = jwtUtils.getUsernameFromToken(token);
                List<String> roles = jwtUtils.getRolesFromToken(token);

                if (roles == null) {
                    roles = new ArrayList<>();  // Prevent null
                }

                // Convert roles to GrantedAuthority
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                // Create authentication object with granted authorities
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, authorities
                );

                // Set authentication details in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
