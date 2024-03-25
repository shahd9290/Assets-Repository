package com.example.assetsystembackend.api.repository;

import com.example.assetsystembackend.api.model.ERole;
import com.example.assetsystembackend.api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing and managing role data in the database.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
