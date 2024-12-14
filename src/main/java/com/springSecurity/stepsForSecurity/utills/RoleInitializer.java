package com.springSecurity.stepsForSecurity.utills;

import com.springSecurity.stepsForSecurity.entity.Roles;
import com.springSecurity.stepsForSecurity.repositoty.RolesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner {

    private final RolesRepository rolesRepository;

    public RoleInitializer(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @Override
    public void run(String... args) {
        createRoleIfNotExists("ROLE_USER");
        createRoleIfNotExists("ROLE_ADMIN");
    }

    private void createRoleIfNotExists(String roleName) {
        if (rolesRepository.findByName(roleName).isEmpty()) {
            Roles role = new Roles();
            role.setName(roleName);
            rolesRepository.save(role);
            System.out.println("Created role: " + roleName);
        } else {
            System.out.println("Role already exists: " + roleName);
        }
    }
}
