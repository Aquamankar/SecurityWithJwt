package com.springSecurity.stepsForSecurity.service.impl;

import com.springSecurity.stepsForSecurity.entity.Roles;
import com.springSecurity.stepsForSecurity.entity.User;
import com.springSecurity.stepsForSecurity.payload.UserDTO;
import com.springSecurity.stepsForSecurity.repositoty.RolesRepository;
import com.springSecurity.stepsForSecurity.repositoty.UserRepository;
import com.springSecurity.stepsForSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.Collections.*;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolesRepository rolesRepository;
//

    @Override
    public UserDTO createOneUser(UserDTO userDTO) {
        // Convert UserDTO to User entity
        User user = mapToEntity(userDTO);

        System.out.println("User saved with roles: " + user);
        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user to the repository
        User savedUser = userRepository.save(user);

        System.out.println("User saved with roles: " + user.getRoles());
        // Convert back to DTO to return
        return mapToDTO(savedUser);
    }

    @Override
    public void deleteusrById(long id) {

        // Find the user by ID
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        // Clear the roles before deleting the user
        user.getRoles().clear();

        userRepository.delete(user);

    }

    // Mapper from UserDTO to User entity
    private User mapToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
    //    user.setRoles(Set.of("USER_ROLE"));
     //   user.setRoles(unmodifiableSet(new HashSet<E>(List.of("USER_ROLE"))));

        Set<Roles> roles= new HashSet<>();

       // Roles userRole=rolesRepository.findByName("ROLE_USER").get();

        Optional<Roles> optionalRole = rolesRepository.findByName("ROLE_USER");
        System.out.println("role for id "+ optionalRole.get());
        if (optionalRole.isEmpty()) {
            throw new RuntimeException("Role ROLE_USER not found in the database");
        }
        roles.add(optionalRole.get());
           System.out.println("role for id "+ roles);
    //    roles.add(userRole);
        user.setRoles(roles);
        System.out.println("Roles assigned to the user: " + user.getRoles());

        return user;
    }

    // Mapper from User entity to UserDTO
    private UserDTO mapToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(userDTO.getPassword());
        return userDTO;
    }
}
