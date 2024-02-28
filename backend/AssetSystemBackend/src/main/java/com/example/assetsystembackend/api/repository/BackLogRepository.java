package com.example.assetsystembackend.api.repository;

import com.example.assetsystembackend.api.model.BackLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BackLogRepository extends JpaRepository<BackLog, Long> {
    List<BackLog> findByAssetId(Long assetId);
}