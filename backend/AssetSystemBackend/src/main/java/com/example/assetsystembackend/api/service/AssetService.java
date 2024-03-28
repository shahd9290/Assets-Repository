package com.example.assetsystembackend.api.service;

import com.example.assetsystembackend.api.model.Asset;
import com.example.assetsystembackend.api.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing assets.
 */
@Service
public class AssetService {

    private final AssetRepository assetsRepo;

    /**
     * Constructs an AssetService with the provided AssetRepository.
     *
     * @param assetsRepo The repository for assets.
     */
    @Autowired
    public AssetService(AssetRepository assetsRepo) {
        this.assetsRepo = assetsRepo;
    }

    /**
     * Retrieves all assets.
     *
     * @return A list of all assets.
     */
    public List<Asset> getAllAssets() {
        return assetsRepo.findAll();
    }

    /**
     * Finds an asset by its ID.
     *
     * @param id The ID of the asset to find.
     * @return An Optional containing the asset if found, otherwise empty.
     */
    public Optional<Asset> findByID(Long id){
        return assetsRepo.findById(id);
    }

    /**
     * Saves a new asset.
     *
     * @param asset The asset to save.
     * @return The ID of the saved asset.
     */
    public long saveNewAsset(Asset asset){
        return assetsRepo.save(asset).getId();
    }

    /**
     * Deletes an asset by its ID.
     *
     * @param id The ID of the asset to delete.
     * @return true if the asset was deleted successfully, false otherwise.
     */
    public boolean deleteAsset(Long id) {
        if (exists(id)) {
            assetsRepo.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Checks if an asset exists by its ID.
     *
     * @param id The ID of the asset to check.
     * @return true if the asset exists, false otherwise.
     */
    public boolean exists (Long id) {return assetsRepo.existsById(id);}

    public void saveExistingAsset(Asset asset) {
        assetsRepo.save(asset);
    }
}
