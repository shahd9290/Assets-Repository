package com.example.assetsystembackend.api.repository;

import com.example.assetsystembackend.api.model.BackLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

/**
 * Repository interface for accessing and managing backlog data in the database.
 */
@Repository
public interface BackLogRepository extends JpaRepository<BackLog, Long> {
    List<BackLog> findByAssetId(Long assetId);

}
