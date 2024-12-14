package com.springSecurity.stepsForSecurity.repositoty;

import com.springSecurity.stepsForSecurity.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsernameOrEmail(String usernameOrEmail, String usernameOrEmail1);


//    @Modifying
//    @Query(value = "DELETE FROM user_roles WHERE user_id = :userId AND roles_id = :roleId", nativeQuery = true)
//    void deleteUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);


    Optional<Object> findByUsername(String username);
}
