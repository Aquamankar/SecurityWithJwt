package com.springSecurity.stepsForSecurity.controller;

import com.springSecurity.stepsForSecurity.entity.Login;
import com.springSecurity.stepsForSecurity.entity.TokenStore;
import com.springSecurity.stepsForSecurity.repositoty.TokenStoreRepository;
import com.springSecurity.stepsForSecurity.utills.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword())
        );
        System.out.println("Authenticated User: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.generateToken(authentication);

        // Save the token to TokenStore
        TokenStore tokenStore = new TokenStore();
        tokenStore.setToken(token);
        tokenStore.setRevoked(false);
        tokenStore.setExpirationTime(jwtUtils.getExpirationDateFromToken(token));
        tokenStoreRepository.save(tokenStore);

        return ResponseEntity.ok(token);
    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
//        // Clear authentication from the SecurityContext
//        SecurityContextHolder.clearContext();
//
//        // Optionally invalidate the token (e.g., add it to a blacklist)
//        String token = request.getHeader("Authorization");
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7); // Remove "Bearer " prefix
//            // Add the token to your blacklist logic here (e.g., save in Redis or a database)
//        }
//
//        // Set the response status and message
//        return ResponseEntity.ok("Logged out successfully");
//    }


    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7); // Remove "Bearer " prefix
        }

        try {
            String newAccessToken = jwtUtils.generateTokenFromRefresh(refreshToken);

            // Optionally, save the new token to TokenStore if you're using token revocation
            TokenStore tokenStore = new TokenStore();
            tokenStore.setToken(newAccessToken);
            tokenStore.setRevoked(false);
            tokenStore.setExpirationTime(jwtUtils.getExpirationDateFromToken(newAccessToken));
            tokenStoreRepository.save(tokenStore);

            return ResponseEntity.ok(newAccessToken);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }



    @Autowired
    private TokenStoreRepository tokenStoreRepository;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        SecurityContextHolder.clearContext();

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            // Mark the token as revoked in the database
            tokenStoreRepository.findByToken(token).ifPresent(tokenStore -> {
                tokenStore.setRevoked(true);
                tokenStoreRepository.save(tokenStore);
            });
        }

        return ResponseEntity.ok("Logged out successfully");
    }
}
