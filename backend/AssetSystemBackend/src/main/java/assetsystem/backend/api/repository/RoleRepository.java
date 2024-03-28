package assetsystem.backend.api.repository;

import assetsystem.backend.api.model.ERole;
import assetsystem.backend.api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing and managing role data in the database.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
