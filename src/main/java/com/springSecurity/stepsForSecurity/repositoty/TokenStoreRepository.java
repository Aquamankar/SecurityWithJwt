package com.springSecurity.stepsForSecurity.repositoty;


import com.springSecurity.stepsForSecurity.entity.TokenStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenStoreRepository extends JpaRepository<TokenStore, Long> {
    Optional<TokenStore> findByToken(String token);
}