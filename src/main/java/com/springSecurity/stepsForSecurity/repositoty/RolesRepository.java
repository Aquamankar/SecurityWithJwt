package com.springSecurity.stepsForSecurity.repositoty;

import com.springSecurity.stepsForSecurity.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Optional;

public interface RolesRepository  extends JpaRepository<Roles,Long> {
   // Map<Object, Object> findByName(String roleUser);

    Optional<Roles> findByName(String name);
}
