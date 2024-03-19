package com.example.assetsystembackend.api.service;

import com.example.assetsystembackend.api.model.Asset;
import com.example.assetsystembackend.api.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetService {

    private final AssetRepository assetsRepo;

    @Autowired
    public AssetService(AssetRepository assetsRepo) {
        this.assetsRepo = assetsRepo;
    }

    public List<Asset> getAllAssets() {
        return assetsRepo.findAll();
    }

    public Optional<Asset> findByID(Long id){
        return assetsRepo.findById(id);
    }

    public long saveNewAsset(Asset asset){
        return assetsRepo.save(asset).getId();
    }

    public boolean deleteAsset(Long id) {
        if (exists(id)) {
            assetsRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean exists (Long id) {return assetsRepo.existsById(id);}
}
