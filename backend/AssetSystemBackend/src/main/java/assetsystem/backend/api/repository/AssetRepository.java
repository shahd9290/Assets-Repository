package assetsystem.backend.api.repository;

import assetsystem.backend.api.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing asset data in the database.
 */
@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
}
