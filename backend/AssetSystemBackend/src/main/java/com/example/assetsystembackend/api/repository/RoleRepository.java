package com.example.assetsystembackend.api.repository;

import com.example.assetsystembackend.api.models.ERole;
import com.example.assetsystembackend.api.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
